package nl.thedutchmc.dutchyhome.tabcompleters;

import org.bukkit.command.CommandSender;

import nl.thedutchmc.dutchycore.module.commands.ModuleTabCompleter;

public class SetHomeCommandCompleter implements ModuleTabCompleter {

	@Override
	public String[] complete(CommandSender sender, String[] args) {
		return new String[] { "<home name>" }; //Only give this one suggestion for tab completion
	}
}
