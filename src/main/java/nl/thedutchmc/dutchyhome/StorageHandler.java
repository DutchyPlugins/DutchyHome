package nl.thedutchmc.dutchyhome;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class StorageHandler {

	private Home plugin;
	
	public StorageHandler(Home plugin) {
		this.plugin = plugin;
	}
	
	private static File storageFile;
	private static FileConfiguration storage;
	
	public static FileConfiguration getStorage() {
		return storage;
	}
	
	public void loadStorage() {
		storageFile = new File(plugin.getDataFolder(), "homes.yml");
		
		if(!storageFile.exists()) {
			storageFile.getParentFile().mkdirs();
			plugin.saveResource("homes.yml", false);
		}
		
		storage = new YamlConfiguration();
		
		try {
			storage.load(storageFile);
			readStorage();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void readStorage() {
		List<String> homes = storage.getStringList("homes");
		
		for(String str : homes) {
			String[] strParts = str.split(":");
			
			Home.homes.put(UUID.fromString(strParts[0]), new Location(Bukkit.getWorld(strParts[4]), Double.valueOf(strParts[1]), Double.valueOf(strParts[2]), Double.valueOf(strParts[3])));
		}
	}
	
	public static void writeHome(UUID uuid, Location loc) {
		List<String> homes = storage.getStringList("homes");
		
		String home = uuid.toString() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getWorld().getName();
		
		boolean alreadyInList = false;
		int inListIndex = 0;
		for(int i = 0; i < homes.size(); i++) {
			UUID homeUuid = UUID.fromString(homes.get(i).split(":")[0]);
			
			if(homeUuid.equals(uuid)) {
				alreadyInList = true;
				inListIndex = i;
			}
		}
		
		if(alreadyInList) {
			homes.set(inListIndex, home);
		} else {
			homes.add(home);
		}
		
		storage.set("homes", homes);
		
		try {
			storage.save(storageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
