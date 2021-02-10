package nl.thedutchmc.dutchyhome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.permissions.PermissionDefault;
import nl.thedutchmc.dutchycore.DutchyCore;
import nl.thedutchmc.dutchycore.module.PluginModule;
import nl.thedutchmc.dutchycore.annotations.Nullable;
import nl.thedutchmc.dutchycore.module.file.ModuleFileHandler;
import nl.thedutchmc.dutchycore.module.file.ModuleStorage;
import nl.thedutchmc.dutchyhome.commands.DelHomeCommandExecutor;
import nl.thedutchmc.dutchyhome.commands.HomeCommandExecutor;
import nl.thedutchmc.dutchyhome.commands.HomesCommandExecutor;
import nl.thedutchmc.dutchyhome.commands.SetHomeCommandExecutor;
import nl.thedutchmc.dutchyhome.listeners.PlayerTransferEventListener;
import nl.thedutchmc.dutchyhome.tabcompleters.DelHomeCommandCompleter;
import nl.thedutchmc.dutchyhome.tabcompleters.HomeCommandCompleter;
import nl.thedutchmc.dutchyhome.tabcompleters.HomesCommandCompleter;
import nl.thedutchmc.dutchyhome.tabcompleters.SetHomeCommandCompleter;

public class DutchyHome extends PluginModule {
	
	private HashMap<UUID, PlayerHomes> playerHomes = new HashMap<>();
	private ModuleStorage storage;
	
	@SuppressWarnings("unchecked")
	@Override
	public void enable(DutchyCore core) {
		super.logInfo("Initializing...");
				
		//Register command executors
		super.registerCommand("home", new HomeCommandExecutor(this), this);
		super.registerCommand("sethome", new SetHomeCommandExecutor(this), this);
		super.registerCommand("homes", new HomesCommandExecutor(this), this);
		super.registerCommand("delhome", new DelHomeCommandExecutor(this), this);
		
		//Register command tab completers
		super.registerTabCompleter("home", new HomeCommandCompleter(this), this);
		super.registerTabCompleter("sethome", new SetHomeCommandCompleter(), this);
		super.registerTabCompleter("homes", new HomesCommandCompleter(), this);
		super.registerTabCompleter("delhome", new DelHomeCommandCompleter(this), this);
		
		//Register permission nodes
		super.registerPermissionNode("dutchyhome.home", PermissionDefault.TRUE, "Allow the usage of /home", null);
		super.registerPermissionNode("dutchyhome.sethome", PermissionDefault.TRUE, "Allow the usage of /sethome", null);
		super.registerPermissionNode("dutchyhome.homes", PermissionDefault.TRUE, "ALlow the usage of /homes", null);
		super.registerPermissionNode("dutchyhome.delhome", PermissionDefault.TRUE, "Allows the usage of /delhome", null);
		
		//'special' nodes
		//Admin node
		HashMap<String, Boolean> adminChildren = new HashMap<>();
		adminChildren.put("dutchyhome.basic", true);
		adminChildren.put("dutchyhome.sethome.amount.infinite", true);
		super.registerPermissionNode("dutchyhome.admin", PermissionDefault.OP, "Admin node for DutchyHome", adminChildren);
		
		//Basic node
		HashMap<String, Boolean> basicChildren = new HashMap<>();
		basicChildren.put("dutchyhome.home", true);
		basicChildren.put("dutchyhome.sethome", true);
		basicChildren.put("dutchyhome.homes", true);
		basicChildren.put("dutchyhome.delhome", true);
		super.registerPermissionNode("dutchyhome.basic", PermissionDefault.TRUE, "Basic node for DutchyHome", basicChildren);
		
		//Read the storage file
		ModuleFileHandler fileHandler = super.getModuleFileHandler();
		this.storage = fileHandler.getModuleStorage();
		this.storage.read();
		
		//Determine if the storage file has a 'homes' key, if not add it
		//Because it didnt have the 'homes' key, we dont need to parse it, since it was empty anyways
		if(storage.getValue("homes") == null) {
			this.storage.setValue("homes", new String[0]);
			this.storage.save();
		} else {
			//Get the value if 'homes'
			Object homesRaw = storage.getValue("homes");
			
			//Determine if we're dealing with a String[] or a List<String>
			//we want a List<String>, so get it into that format
			List<String> homes = null;
			if(homesRaw instanceof List<?>) {
				homes = (List<String>) homesRaw;
			} else if(homesRaw instanceof String[]) {
				homes = Arrays.asList((String[]) homesRaw);
				
				// https://stackoverflow.com/a/5755510/10765090
				homes = new ArrayList<>(homes);
			}
			
			//Loop over the items in the List
			for(String home : homes) {
				//Split on the seperator
				String[] homeParts = home.split("<-=->");
				
				//Get the individual components
				UUID uuid = UUID.fromString(homeParts[0]);
				String homeName = homeParts[1];
				String worldName = homeParts[2];
				int x = Integer.valueOf(homeParts[3]);
				int y = Integer.valueOf(homeParts[4]);
				int z = Integer.valueOf(homeParts[5]);
				
				//Construct a Location object
				Location l = new Location(Bukkit.getWorld(worldName), x, y, z);
				
				//Check if the playerHomes list already has the player in it,
				//if so we add the newly parsed home to the list of homes they own
				if(this.playerHomes.containsKey(uuid)) {
					PlayerHomes playerHomes = this.playerHomes.get(uuid);
					playerHomes.setHome(homeName, l);
					
					this.playerHomes.put(uuid, playerHomes);
				} else {
					//The player was not yet in the list of PlayerHomes, so create a new PlayerHomes
					//and add the newly parsed home to it
					PlayerHomes playerHomes = new PlayerHomes(uuid);
					playerHomes.setHome(homeName, l);
					
					this.playerHomes.put(uuid, playerHomes);
				}		
			}
		}
		
		super.logInfo("Initialization complete!");
	}
	
	@Override
	public void postEnable() {
		//Register event listeners
		if(super.isModuleRegistered("OfflinePlayers")) {
			super.logInfo("OfflinePlayers is installed. Enabling listener!");
			super.registerModuleEventListener(new PlayerTransferEventListener(this));
		}
	}
	
	/**
	 * Write a home to disk
	 * @param homeName The name of the home
	 * @param uuid The UUID of the home owner
	 * @param location The Location of the home
	 */
	@SuppressWarnings("unchecked")
	public void writeHome(String homeName, UUID uuid, Location location) {
		//Serialize the provided values into a String
		String seperator = "<-=->";
		String write = uuid.toString() + seperator + homeName + seperator + location.getWorld().getName() + seperator + location.getBlockX() + seperator + location.getBlockY() + seperator + location.getBlockZ();
		
		//Get the current list of 'homes' in the storage file
		//Get it into the format of a List<String>
		Object homesRaw = this.storage.getValue("homes");
		List<String> homes = null;
		if(homesRaw instanceof List<?>) {
			homes = (List<String>) homesRaw;
		} else if(homesRaw instanceof String[]) {
			homes = Arrays.asList((String[]) homesRaw);
			
			// https://stackoverflow.com/a/5755510/10765090
			homes = new ArrayList<>(homes);
		}
		
		//Add the new home to it
		homes.add(StringEscapeUtils.escapeJava(write));
		
		//Set in storage and save
		this.storage.setValue("homes", homes.toArray(new String[0]));
		this.storage.save();
	}
	
	/**
	 * Write all playerHomes currently in memory to disk
	 */
	public void writeMemory() {
		List<String> homesSerialized = new ArrayList<>();
		String seperator = "<-=->";

		this.playerHomes.values().forEach(playerHomesObject -> {
			playerHomesObject.getAllHomes().forEach((k, v) -> {
				String write = playerHomesObject.getOwner().toString() + seperator + k + seperator + v.getWorld().getName() + seperator + v.getBlockX() + seperator + v.getBlockY() + seperator + v.getBlockZ();
				homesSerialized.add(write);
			});
		});
		
		this.storage.setValue("homes", homesSerialized.toArray(new String[0]));
		this.storage.save();
	}
	
	/**
	 * Get PlayerHomes for a player
	 * @param uuid The UUID of the player
	 * @return The associated PlayerHomes
	 */
	@Nullable
	public PlayerHomes getPlayerHomes(UUID uuid) {
		return this.playerHomes.get(uuid);
	}
	
	/**
	 * Set a PlayerHomes for a player
	 * @param uuid The UUID of the player
	 * @param playerHomes The PlayerHomes object to set
	 */
	public void setPlayerHome(UUID uuid, PlayerHomes playerHomes) {
		this.playerHomes.put(uuid, playerHomes);
	}
	
	/**
	 * Remove a PlayerHomes for a player
	 * @param uuid The UUID of the player
	 */
	public void delPlayerHome(UUID uuid) {
		this.playerHomes.remove(uuid);
	}
}
