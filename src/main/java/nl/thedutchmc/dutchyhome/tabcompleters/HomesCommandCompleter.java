package nl.thedutchmc.dutchyhome.tabcompleters;

import org.bukkit.command.CommandSender;

import nl.thedutchmc.dutchycore.module.commands.ModuleTabCompleter;

public class HomesCommandCompleter implements ModuleTabCompleter {

	@Override
	public String[] complete(CommandSender sender, String[] args) {
		return null; //Dont want tab completion here
	}
}
