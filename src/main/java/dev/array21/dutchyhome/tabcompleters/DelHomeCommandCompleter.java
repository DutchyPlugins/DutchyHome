package dev.array21.dutchyhome.tabcompleters;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.array21.dutchycore.module.commands.ModuleTabCompleter;
import dev.array21.dutchyhome.DutchyHome;
import dev.array21.dutchyhome.PlayerHomes;

public class DelHomeCommandCompleter implements ModuleTabCompleter {

	DutchyHome module;
	
	public DelHomeCommandCompleter(DutchyHome module) {
		this.module = module;
	}
	
	@Override
	public String[] complete(CommandSender sender, String[] args) {
		//Check if the sender is a player
		if(!(sender instanceof Player)) {
			return null;
		}
		
		//Get the player and their PlayerHomes object
		Player player = (Player) sender;
		PlayerHomes playerHomes = this.module.getPlayerHomes(player.getUniqueId());
		
		//if they dont have a playerHomes, return null
		if(playerHomes == null) {
			return null;
		}
		
		//Get the names of all homes owned by the player
		return playerHomes.getAllHomes().keySet().toArray(new String[0]);
	}
}
