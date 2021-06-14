package dev.array21.dutchyhome.tabcompleters;

import org.bukkit.command.CommandSender;

import dev.array21.dutchycore.module.commands.ModuleTabCompleter;

public class HomesCommandCompleter implements ModuleTabCompleter {

	@Override
	public String[] complete(CommandSender sender, String[] args) {
		return null; //Dont want tab completion here
	}
}
