package nl.thedutchmc.dutchyhome.commands;

import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import nl.thedutchmc.dutchycore.Triple;
import nl.thedutchmc.dutchycore.module.commands.ModuleCommand;
import nl.thedutchmc.dutchycore.utils.Utils;
import nl.thedutchmc.dutchyhome.DutchyHome;
import nl.thedutchmc.dutchyhome.PlayerHomes;

/**
 * Handles execution for:
 * <pre> /sethome <name> </pre>
 *
 */
public class SetHomeCommandExecutor implements ModuleCommand {

	private DutchyHome module;
	
	public SetHomeCommandExecutor(DutchyHome module) {
		this.module = module;
	}
	
	@Override
	public boolean fire(CommandSender sender, String[] args) {
		
		//Check permissions
		if(!sender.hasPermission("dutchyhome.sethome")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
			return true;
		}
		
		//Check if the sender is a Player
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You can only use this command as an in-game Player!");
			return true;
		}
		
		//Get the Player and their PlayerHomes object
		Player player = (Player) sender;
		PlayerHomes playerHomes = this.module.getPlayerHomes(player.getUniqueId());
		
		//If playerHomes is null, create a new oen
		if(playerHomes == null) {
			playerHomes = new PlayerHomes(player.getUniqueId());
		}
		
		//TODO this should be configurable
		int maxHomes = 3;
		
		//Loop over all permission the player has
		//We're looking for `dutchyhome.sethome.amount.n', where n is an integer
		//indicating the max number of homes
		boolean maxHomesNonDefault = false;
		for(PermissionAttachmentInfo permInfo : sender.getEffectivePermissions()) {
			String permissionName = permInfo.getPermission();
			
			if(!permissionName.startsWith("dutchyhome.sethome.amount")) {
				continue;
			}
			
			//Split the permission name
			String[] parts = permissionName.split(Pattern.quote("."));
			
			//if they have the permission node 'dutchyhome.sethome.amount.infinite', there is no limit
			if(parts[parts.length -1].equalsIgnoreCase("infinite")) {
				maxHomes = Integer.MAX_VALUE;
				break;
			}
			
			//Verify that the amount is an integer
			if(!Utils.verifyPositiveInteger(parts[parts.length -1])) {
				sender.sendMessage("Please contact your server administrator. They have made a mistake with permissions for DutchyHome!");
				return true;
			}

			//Get the String as an Integer
			int potentialMaxHomes = Integer.valueOf(parts[parts.length -1]);
			
			//If the potential max homes is larger than the maxhomes, that will become the new maxhomes
			if(potentialMaxHomes > maxHomes) {
				maxHomes = potentialMaxHomes;
			}
			
			//If the potential max homes is less than the maxhomes, and it has not yet been lowered, that will be the new value
			if(potentialMaxHomes < maxHomes && !maxHomesNonDefault) {
				maxHomesNonDefault = true;
				potentialMaxHomes = maxHomes;
			}
		}
		
		//Check if the player has reached the home limit
		int amountOfHomes = playerHomes.getAllHomes().size();
		if(amountOfHomes >= maxHomes) {
			sender.sendMessage("You have reached the limit of homes you can set! Remove a home before setting a new one.");
			return true;
		}
		
		//If the player provided a name for the home, use that, else use 'home'
		String homeName = "home";
		if(args.length >= 1) {
			homeName = args[0];
		}
		
		//Get the players current location
		Location playerCurrentLoc = player.getLocation();
		
		//Set the home in memory
		playerHomes.setHome(homeName, playerCurrentLoc);
		
		//Write to the disk
		this.module.setPlayerHome(player.getUniqueId(), playerHomes);
		this.module.writeHome(homeName, player.getUniqueId(), playerCurrentLoc);
		
		//Send the message to the player
		String message = "Set home %s at %d, %d, %d!";
		message = Utils.processColours(message, 
				new Triple<String, ChatColor, ChatColor>("%s", ChatColor.RED, ChatColor.GOLD), 
				new Triple<String, ChatColor, ChatColor>("%d", ChatColor.RED, ChatColor.GOLD));
		sender.sendMessage(String.format(message, homeName, playerCurrentLoc.getBlockX(), playerCurrentLoc.getBlockY(), playerCurrentLoc.getBlockZ()));
		
		return true;
	}

}
