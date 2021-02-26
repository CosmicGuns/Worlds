package net.velinquish.worlds;

import java.util.List;

import org.bukkit.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Area {
	@Getter
	private String name;
	@Getter
	private String id;
	@Getter
	private Location loc;
	@Getter
	private List<String> lore;

	public Area setLoc(Location loc) {
		this.loc = loc;
		return this;
	}
}
