package nl.thedutchmc.dutchyhome;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	
	public static void readStorage() {
		List<String> homes = storage.getStringList("homes");
		Home.homes.clear();
		
		for(String str : homes) {
			String[] strParts = str.split("<==>");
			
			HomeObject home = new HomeObject(UUID.fromString(strParts[0]), strParts[1], new Location(Bukkit.getWorld(strParts[5]), Double.valueOf(strParts[2]), Double.valueOf(strParts[3]), Double.valueOf(strParts[4])));
			Home.homes.add(home);
		}
	}
	
	public static void writeHome(HomeObject homeObj) {
		List<String> homes = storage.getStringList("homes");
		
		//Format:
		// OwnerUUID : HomeName : X : Y : Z : WorldName
		
		Location loc = homeObj.getLocation();
		UUID uuid = homeObj.getOwner();
		String name = homeObj.getName();
		
		System.out.println(uuid);
		
		String home = String.valueOf(uuid) + "<==>" + name + "<==>" + loc.getX() + "<==>" + loc.getY() + "<==>" + loc.getZ() + "<==>" + loc.getWorld().getName();
		
		boolean alreadyInList = false;
		int inListIndex = 0;
		for(int i = 0; i < homes.size(); i++) {
			String[] parts = homes.get(i).split("<==>");
			UUID homeUuid = UUID.fromString(parts[0]);
			String homeName = parts[1];
			
			if(homeUuid.equals(uuid) && homeName.equals(name)) {
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
	
	public static void homeDeleted() {
		List<String> homesSerialized = new ArrayList<>();
		
		for(HomeObject home : Home.homes) {
			Location loc = home.getLocation();
			String homeSerialized = String.valueOf(home.getOwner()) + "<==>" + home.getName() + "<==>" + loc.getX() + "<==>" + loc.getY() + "<==>" + loc.getZ() + "<==>" + loc.getWorld().getName();
			homesSerialized.add(homeSerialized);
		}
		
		storage.set("homes", homesSerialized);
		
		try {
			storage.save(storageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
