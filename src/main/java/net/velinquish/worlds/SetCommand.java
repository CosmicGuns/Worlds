package net.velinquish.worlds;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import lombok.AllArgsConstructor;
import net.velinquish.utils.AnyCommand;
import net.velinquish.utils.Common;

@AllArgsConstructor
public class SetCommand extends AnyCommand {

	Worlds plugin;

	@Override
	protected void run(CommandSender sender, String[] args, boolean silent) {
		checkPermission(plugin.getPermission());
		checkArgs(2, plugin.getLangManager().getNode("command-set-usage"));

		Location loc = getLocation(2, plugin.getLangManager().getNode("command-set-usage"), plugin.getLangManager().getNode("command-set-console-usage"));

		Area previous = plugin.unloadArea(args[1]);
		if (previous == null)
			plugin.loadArea(new Area("&3Unnamed Area", args[1], loc, Arrays.asList(new String[] {"","&7Configure the GUI item", "&7in &b&nareas.yml&7."})));
		else
			plugin.loadArea(previous.setLoc(loc));
		plugin.saveArea(args[1], loc);

		tell(plugin.getLangManager().getNode("location-set").replace(Common.map("%area%", args[1], "%x%", "" + loc.getX(),
				"%y%", "" + loc.getY(), "%z%", "" + loc.getZ())));
	}

}
