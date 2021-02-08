package nl.thedutchmc.dutchyhome.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.thedutchmc.dutchycore.Triple;
import nl.thedutchmc.dutchycore.module.commands.ModuleCommand;
import nl.thedutchmc.dutchycore.utils.Utils;
import nl.thedutchmc.dutchyhome.DutchyHome;
import nl.thedutchmc.dutchyhome.PlayerHomes;

/**
 * Provides execution for:
 * <pre> /homes </pre>
 */
public class HomesCommandExecutor implements ModuleCommand {

	private DutchyHome module;
	
	public HomesCommandExecutor(DutchyHome module) {
		this.module = module;
	}
	
	@Override
	public boolean fire(CommandSender sender, String[] args) {
		
		//Check permissions
		if(!sender.hasPermission("dutchyhome.homes")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}
		
		//Check if the sender is a player
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be used by in-game Players!");
			return true;
		}
		
		//Get the player and their PlayerHomes object
		Player player = (Player) sender;
		PlayerHomes homes = this.module.getPlayerHomes(player.getUniqueId());
		
		//If PlayerHomes is null, or homes.getAllHomes()'s size is 0, the Player has no homes
		if(homes == null || homes.getAllHomes().size() == 0) {
			sender.sendMessage(ChatColor.RED + "You do not have any homes!");
			return true;
		}
		
		//Loop over all the homes of the player and display it with nice formatting
		homes.getAllHomes().forEach((k, v) -> {
			String message = "%s - World: %s, X: %d, Y: %d, Z: %d";
			
			//Colour substitution
			message = Utils.processColours(message, 
					new Triple<String, ChatColor, ChatColor>("%s", ChatColor.RED, ChatColor.GOLD), 
					new Triple<String, ChatColor, ChatColor>("%d", ChatColor.RED, ChatColor.GOLD));
			
			//Format and send
			sender.sendMessage(String.format(message, k, v.getWorld().getName(), v.getBlockX(), v.getBlockY(), v.getBlockZ()));
		});
		
		return true;
	}	
}
