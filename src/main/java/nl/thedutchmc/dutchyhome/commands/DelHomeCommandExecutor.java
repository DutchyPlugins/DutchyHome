package nl.thedutchmc.dutchyhome.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.thedutchmc.dutchycore.module.commands.ModuleCommand;
import nl.thedutchmc.dutchyhome.DutchyHome;
import nl.thedutchmc.dutchyhome.PlayerHomes;

/**
 * Handles execution for:
 * <pre> /delhome &lt;home name&gt; </pre>
 */
public class DelHomeCommandExecutor implements ModuleCommand {

	DutchyHome module;
	
	public DelHomeCommandExecutor(DutchyHome module) {
		this.module = module;
	}
	
	@Override
	public boolean fire(CommandSender sender, String[] args) {
		
		//Permission checl
		if(!sender.hasPermission("dutchyhome.delhome")) {
			sender.sendMessage("You do not have permission to use this command!");
			return true;
		}
		
		//Check if the sender is a Player
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be used by in-game Players!");
			return true;
		}
		
		//Check if the player provided the home name to remove
		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments! Usage: /delhome <home name>");
			return true;
		}
		
		//Get the player and their playerHomes object
		Player player = (Player) sender;
		PlayerHomes playerHomes = this.module.getPlayerHomes(player.getUniqueId());
		
		if(playerHomes == null || playerHomes.getAllHomes().size() == 0) {
			sender.sendMessage(ChatColor.RED + "You don't have any homes!");
			return true;
		}
		
		//Get the homename
		String homeName = args[0];
		
		//Check if the home exists
		boolean homeExists = false;
		for(String name : playerHomes.getAllHomes().keySet()) {
			if(name.equalsIgnoreCase(homeName)) {
				homeExists = true;
				break;
			}
		}
		
		//If the home doesn't exist, tell the player and return
		if(!homeExists) {
			sender.sendMessage(ChatColor.RED + "Home does not exist!");
			return true;
		}
		
		//Delete the home
		playerHomes.delHome(homeName);
		this.module.setPlayerHome(player.getUniqueId(), playerHomes);
		
		//Write memory to disk
		this.module.writeMemory();
		
		return true;
	}
}
