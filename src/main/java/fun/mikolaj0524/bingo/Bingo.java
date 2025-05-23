package fun.mikolaj0524.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static fun.mikolaj0524.bingo.GameLogic.setupLogic;

import static fun.mikolaj0524.bingo.Lobby.*;
import static fun.mikolaj0524.bingo.MapGenerator.teleportNewWorld;
import static fun.mikolaj0524.bingo.MapManager.*;
import static fun.mikolaj0524.bingo.Messages.getFromConfig;
import static fun.mikolaj0524.bingo.Messages.getPrefix;

public final class Bingo extends JavaPlugin implements Listener {

	private static Bingo pluginInstance;
	public static FileConfiguration cfg = null;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		cfg = getConfig();

		getServer().getLogger().info(ChatColor.GREEN + "Bingo > Start!");

		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new MapManager(), this);
		getServer().getPluginManager().registerEvents(new PlayerManager(), this);
		getServer().getPluginManager().registerEvents(new Lobby(), this);
		getServer().getPluginManager().registerEvents(new GameLogic(), this);
		pluginInstance = this;

		if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
			PlaceholderAPIHook.registerHook();
		}

		loadItems();
		loadSings();
		createSigns();
		setupLogic();
	}

	public static Bingo getPluginInstance(){
		return pluginInstance;
	}

	@Override
	public void onDisable() {
		getServer().getLogger().info(ChatColor.RED + "Bingo > Stop!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if(player instanceof Player){

			if(command.getName().equalsIgnoreCase("bingo_tp")){
				teleportNewWorld(player);
				return true;
			}

			if(command.getName().equalsIgnoreCase("bingo_rlcf")){
				if(player.isOp()){
					reloadConfig();
					cfg = getConfig();
					Messages.toPlayer(player, "Successfully reloaded bingo plugin!");
				}
				else{
					Messages.toPlayer(player, getFromConfig("messages.no_perms"));
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 1.0f);
				}
				return true;
			}
		}
		return false;
	}

}
