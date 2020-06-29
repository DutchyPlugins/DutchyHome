package nl.thedutchmc.dutchyhome;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Home extends JavaPlugin {

	public static HashMap<UUID, Location> homes = new HashMap<>();
	
	@Override
	public void onEnable() {
		
		Home plugin = this;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				StorageHandler sH = new StorageHandler(plugin);
				sH.loadStorage();
			}
		}.runTaskLater(this, 5);
		
		getCommand("home").setExecutor(new CommandHandler());
		getCommand("sethome").setExecutor(new CommandHandler());	
	}
}
