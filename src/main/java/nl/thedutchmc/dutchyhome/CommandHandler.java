package nl.thedutchmc.dutchyhome;

import java.util.UUID;

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
			
			String name = "";
			if(args.length == 0) {
				name = "home";
			} else {
				name = args[0];
			}
			
			Player senderP = (Player) sender;
			
			boolean anyHomeExists = false;
			for(HomeObject home : Home.homes) {
				if(!home.getName().equals(name) || !home.getOwner().equals(senderP.getUniqueId())) {
					continue;
				}
				
				anyHomeExists = true;
				
				SuccessfulHomeTeleportEvent event = new SuccessfulHomeTeleportEvent(senderP, senderP.getLocation());
				Bukkit.getPluginManager().callEvent(event);
				
				sender.sendMessage(ChatColor.GOLD + "Teleporting...");
				senderP.teleport(home.getLocation());
			}
			
			if(!anyHomeExists) {
				sender.sendMessage(ChatColor.GOLD + "You don't have any home set or the name of the home you entered does not exist. You can set a home using " + ChatColor.RED +"/sethome");
			}
			
			return true;
			
		} else if(command.getName().equals("sethome")) {
			
			String name = "";
			if(args.length == 0) {
				name = "home";
			} else {
				name = args[0];
			}
			
			Player senderP = (Player) sender;
			
			boolean homeExists = false;
			HomeObject existingHome = null;
			
			int count = 0;
			for(HomeObject home : Home.homes) {
				if(home.getOwner().equals(senderP.getUniqueId())) {
					count++;
				}
				
				if(home.getName().equals(name) && home.getOwner().equals(senderP.getUniqueId())) {
					homeExists = true;
					existingHome = home;
					break;
				}
			}
			
			//Player already owns a Home by that name, remove it from the list
			if(homeExists) {
				Home.homes.remove(existingHome);
			}
			
			if(!homeExists && count >= 3) {
				sender.sendMessage(ChatColor.GOLD + "You have reached the maximum amounts of homes! You must delete an existing home to set a new one!");
				return true;
			}
			
			HomeObject home = new HomeObject(senderP.getUniqueId(), name, senderP.getLocation());
			
			Home.homes.add(home);
			StorageHandler.writeHome(home);
		
			sender.sendMessage(ChatColor.GOLD + "Created a Home called " + ChatColor.RED + name + ChatColor.GOLD + " at your current location!");

			return true;
		} else if(command.getName().equals("listhome")) {
			sender.sendMessage(ChatColor.GOLD + "Your homes:");
			
			for(HomeObject home : Home.homes) {
				if(home.getOwner().equals(((Player) sender).getUniqueId())) {
					sender.sendMessage(ChatColor.GOLD + "- " + ChatColor.RED + home.getName());
				}
			}
			
			return true;
		} else if(command.getName().equals("delhome")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "You need to specify which Home you want to delete!");
				return true;
			}
			
			String homeName = args[0];
			UUID owner = ((Player) sender).getUniqueId();
			
			boolean homeExists = true;
			for(HomeObject home : Home.homes) {
				if(home.getOwner().equals(owner) && home.getName().equals(homeName)) {
					Home.homes.remove(home);
					StorageHandler.homeDeleted();
					
					homeExists = true;
					
					sender.sendMessage(ChatColor.GOLD + "Deleted home " + ChatColor.RED + homeName);
					break;
				}
			}
			
			if(!homeExists) {
				sender.sendMessage(ChatColor.GOLD + "Home does not exists!");
			}
			
			return true;
		}
		return false;
	}

}
