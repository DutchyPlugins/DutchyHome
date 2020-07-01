package nl.thedutchmc.dutchyhome;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandHandler implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(command.getName().equals("home")) {
			
			Player senderP = (Player) sender;
			
			if(Home.homes.containsKey(senderP.getUniqueId())) {
				
				SuccessfulHomeTeleportEvent event = new SuccessfulHomeTeleportEvent(senderP, senderP.getLocation());
				Bukkit.getPluginManager().callEvent(event);
				
				sender.sendMessage(ChatColor.GOLD + "Teleporting...");
				senderP.teleport(Home.homes.get(senderP.getUniqueId()));
				
			} else {
				sender.sendMessage(ChatColor.GOLD + "You don't have any home set, you can set one using " + ChatColor.RED +"/sethome");
			}
			
			return true;
			
			
		} else if(command.getName().equals("sethome")) {
			
			Player senderP = (Player) sender;
			
			Home.homes.put(senderP.getUniqueId(), senderP.getLocation());
			StorageHandler.writeHome(senderP.getUniqueId(), senderP.getLocation());
		
			sender.sendMessage(ChatColor.GOLD + "Set your home to your current position!");
			
			return true;
		}
		return false;
	}

}
