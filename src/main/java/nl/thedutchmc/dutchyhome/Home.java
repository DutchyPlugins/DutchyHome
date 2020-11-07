package nl.thedutchmc.dutchyhome;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Home extends JavaPlugin {

	public static List<HomeObject> homes = new ArrayList<>();
	
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
		getCommand("listhome").setExecutor(new CommandHandler());
		getCommand("delhome").setExecutor(new CommandHandler());
		
		new BukkitRunnable() {
			@Override
			public void run() {
				StorageHandler.readStorage();
			}
		}.runTaskTimerAsynchronously(plugin, 20, 50*60*20);
		
	}
}
