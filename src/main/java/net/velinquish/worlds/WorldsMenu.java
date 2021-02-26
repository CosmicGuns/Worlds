package net.velinquish.worlds;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.designer.menu.impl.MenuPagged;
import org.mineacademy.designer.model.ItemCreator;
import org.mineacademy.remain.model.CompDye;

import net.velinquish.utils.Common;

public class WorldsMenu extends MenuPagged<Area> {

	Worlds plugin;

	protected WorldsMenu(Worlds plugin, List<Area> areas) {
		super(2 * 9, null, areas);
		this.plugin = plugin;
	}

	@Override
	protected String getTitlePrefix() {
		return "&dTeleport";
	}

	@Override
	protected ItemStack convertToItemStack(Area item) {
		return ItemCreator.of(Material.STAINED_GLASS_PANE, item.getName(), item.getLore().toArray(new String[item.getLore().size()]))
				.color(CompDye.PURPLE).build().make();
	}

	@Override
	protected void onPageClick(Player player, Area item, ClickType click) {
		player.closeInventory();
		if (player.hasPermission(plugin.getAreasAccessPermission().replaceAll("%area%", item.getId()))) {
			player.teleport(item.getLoc());
			plugin.getLangManager().getNode("teleported-to-area").replace(Common.map("%area%", item.getName())).execute(player);
		} else
			plugin.getLangManager().getNode("no-access-to-area").replace(Common.map("%area%", item.getName())).execute(player);
	}

	@Override
	protected boolean updateButtonOnClick() {
		return false;
	}

	@Override
	protected String[] getInfo() {
		return null;
	}

}
