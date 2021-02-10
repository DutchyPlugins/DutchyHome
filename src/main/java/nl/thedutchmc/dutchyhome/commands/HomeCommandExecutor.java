package nl.thedutchmc.dutchyhome.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.thedutchmc.dutchycore.module.commands.ModuleCommand;
import nl.thedutchmc.dutchyhome.DutchyHome;
import nl.thedutchmc.dutchyhome.PlayerHomes;
import nl.thedutchmc.dutchyhome.events.HomeTeleportEvent;

/**
 * Provides execution for:
 * <pre> /home &lt;homename&gt; </pre>
 */
public class HomeCommandExecutor implements ModuleCommand {

	private DutchyHome module;
	
	public HomeCommandExecutor(DutchyHome module) {
		this.module = module;
	}
	
	@Override
	public boolean fire(CommandSender sender, String[] args) {
		
		//Check if the sender has permission
		if(!sender.hasPermission("dutchyhome.home")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
			return true;
		}
		
		//Check if the sender is a Player
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only in-game Players can use this command!");
			return true;
		}
		
		//Check if the sender provided the name of the home to teleport to
		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Please provide the name of the home you want to teleport to!");
			return true;
		}
		
		//Get the home name from args
		String homeName = args[0];
		
		//Get the UUID and the PlayerHomes object for the player
		UUID uuid = ((Player) sender).getUniqueId();
		PlayerHomes playerHomes = this.module.getPlayerHomes(uuid);
		
		//if the PlayerHomes object is null, the Player has no homes
		if(playerHomes == null) {
			sender.sendMessage(ChatColor.RED + "You do not have any homes!");
			return true;
		}
		
		//Get the home for the provided name
		Location home = playerHomes.getHome(homeName);
		
		//if home is null, the homeName has no associated location (i.e. the home does not exist)
		if(home == null) {
			sender.sendMessage(ChatColor.RED + "Unknown home!");
			return true;
		}
		
		Player player = (Player) sender;
		Location preTeleportLocation = player.getLocation();
		
		//Teleport the player
		player.teleport(home);
		
		//Fire HomeTeleportEvent
		this.module.throwModuleEvent(new HomeTeleportEvent(player, preTeleportLocation, home));
		
		sender.sendMessage(ChatColor.GOLD + "Teleporting...");
		
		return true;
	}
}
