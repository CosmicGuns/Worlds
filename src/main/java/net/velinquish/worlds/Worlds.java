package net.velinquish.worlds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.designer.Designer;

import lombok.Getter;
import net.velinquish.utils.Common;
import net.velinquish.utils.VelinquishPlugin;
import net.velinquish.utils.lang.LangManager;

public class Worlds extends JavaPlugin implements Listener, VelinquishPlugin {

	@Getter
	private static Worlds instance;
	@Getter
	private LangManager langManager;

	@Getter
	private String prefix;
	@Getter
	private String permission;
	@Getter
	private String areasAccessPermission;
	@Getter
	private String areasSeePermission;

	private static boolean debug;

	@Getter
	private YamlConfiguration config;
	private File configFile;

	@Getter
	private YamlConfiguration lang;
	private File langFile;

	private YamlConfiguration areasConfiguration;
	private File areasFile;

	@Getter
	private List<Area> areas;

	@Override
	public void onEnable() {
		instance = this;
		Common.setInstance(this);
		Designer.setPlugin(this);

		langManager = new LangManager();

		areas = new ArrayList<>();

		try {
			loadFiles();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		getServer().getPluginManager().registerEvents(this, this);
		Common.registerCommand(new WorldsCommand(this));
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public void loadFiles() throws IOException, InvalidConfigurationException {
		configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			saveResource("config.yml", false);
		}
		config = new YamlConfiguration();
		config.load(configFile);

		prefix = getConfig().getString("plugin-prefix");
		debug = getConfig().getBoolean("debug");
		permission = getConfig().getString("permission");
		areasAccessPermission = getConfig().getString("area-permission");
		areasSeePermission = getConfig().getString("areas-see-permission");

		langFile = new File(getDataFolder(), "lang.yml");
		if (!langFile.exists()) {
			langFile.getParentFile().mkdirs();
			saveResource("lang.yml", false);
		}
		lang = new YamlConfiguration();
		lang.load(langFile);

		langManager.clear();
		langManager.setPrefix(prefix);
		langManager.loadLang(lang);

		areasFile = new File(getDataFolder(), "areas.yml");
		if (!areasFile.exists()) {
			areasFile.getParentFile().mkdirs();
			saveResource("areas.yml", false);
		}
		areasConfiguration = new YamlConfiguration();
		areasConfiguration.load(areasFile);

		areas.clear();
		loadAreas();
	}

	private void loadAreas() {
		for (String area : areasConfiguration.getConfigurationSection("Areas").getKeys(false)) {
			loadArea(new Area(areasConfiguration.getString("Areas." + area + ".Name"),
					area,
					(Location) areasConfiguration.get("Areas." + area + ".Location"),
					areasConfiguration.getStringList("Areas." + area + ".Lore")));
			debug("Loaded in area " + area);
		}
	}

	public void loadArea(Area area) {
		areas.add(area);
	}

	public Area unloadArea(String area) {
		for (Area a : areas)
			if (a.getId().equals(area)) {
				areas.remove(a);
				return a;
			}
		return null;
	}

	public void saveArea(String area, Location loc) {
		areasConfiguration.set("Areas." + area + ".Location", loc);
		try {
			areasConfiguration.save(areasFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//	@EventHandler
	//	public void onJoin(PlayerJoinEvent e) {
	//
	//	}

	public static void debug(String message) {
		if (debug == true)
			Common.log(message);
	}
}
