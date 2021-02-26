package net.velinquish.worlds;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import net.velinquish.utils.Common;

public class WorldsCommand extends Command {

	Worlds plugin;

	protected WorldsCommand(Worlds plugin) {
		super(plugin.getConfig().getString("main-command"));
		setAliases(plugin.getConfig().getStringList("plugin-aliases"));
		setDescription("Main Worlds command");
		setUsage("/worlds");

		this.plugin = plugin;
	}

	//TODO Create reload command
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if (args.length < 1) {
			if (!(sender instanceof Player)) {
				plugin.getLangManager().getNode("not-a-player").execute(sender);
				return false;
			}

			Player player = (Player) sender;

			String perm = plugin.getPermission().replaceAll("%action%", "open");
			if (!player.hasPermission(perm)) {
				plugin.getLangManager().getNode("no-permission").replace(Common.map("%permission%", perm)).execute(player);
				return false;
			}

			new WorldsMenu(plugin, plugin.getAreas().stream()
					.filter((area) -> player.hasPermission(plugin.getAreasSeePermission().replaceAll("%area%", area.getId()))).collect(Collectors.toList()))
			.displayTo(player);
		} else if ("set".equalsIgnoreCase(args[0]))
			new SetCommand(plugin).execute(sender, args, Arrays.asList(args).contains("-s"));
		else if ("reload".equalsIgnoreCase(args[0])) {
			String perm = plugin.getPermission().replaceAll("%action%", "reload");
			if (!sender.hasPermission(plugin.getPermission().replaceAll("%action%", "reload"))) {
				plugin.getLangManager().getNode("no-permission").replace(Common.map("%permission%", perm)).execute(sender);
				return false;
			}

			try {
				plugin.loadFiles();
			} catch (IOException | InvalidConfigurationException e) {
				Common.tell(sender, "&cAn error occurred when reloading files for Worlds.");
				e.printStackTrace();
				return false;
			}

			plugin.getLangManager().getNode("plugin-reloaded").execute(sender);
		}
		else
			plugin.getLangManager().getNode("command-worlds-usage").execute(sender);

		return false;
	}

}
