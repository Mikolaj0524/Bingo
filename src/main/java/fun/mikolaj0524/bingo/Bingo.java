package fun.mikolaj0524.bingo;

import fun.mikolaj0524.bingo.Elements.*;
import fun.mikolaj0524.bingo.Events.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static fun.mikolaj0524.bingo.Elements.GameLogic.setupLogic;

import static fun.mikolaj0524.bingo.Elements.Lobby.*;
import static fun.mikolaj0524.bingo.Elements.MapManager.*;

public final class Bingo extends JavaPlugin implements Listener {

	private static Bingo pluginInstance;
	public static FileConfiguration cfg = null;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		cfg = getConfig();

		getServer().getLogger().info("Bingo > Start!");

		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new MapManager(), this);
		getServer().getPluginManager().registerEvents(new Lobby(), this);
		getServer().getPluginManager().registerEvents(new GameLogic(), this);

		getServer().getPluginManager().registerEvents(new Drop(), this);
		getServer().getPluginManager().registerEvents(new InventoryClick(), this);
		getServer().getPluginManager().registerEvents(new ItemPickup(), this);
		getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
		getServer().getPluginManager().registerEvents(new PlayerRightClickSign(), this);
		getServer().getPluginManager().registerEvents(new Swap(), this);

		pluginInstance = this;
		if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
			PlaceholderAPIHook.registerHook();
		}
		loadItems();
		loadLocations();
		createSigns();
		setupLogic();
	}

	public static Bingo getPluginInstance(){
		return pluginInstance;
	}

	@Override
	public void onDisable() {
		getServer().getLogger().info("Bingo > Stop!");
	}

}
