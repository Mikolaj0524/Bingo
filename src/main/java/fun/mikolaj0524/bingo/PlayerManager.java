package fun.mikolaj0524.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;

import static fun.mikolaj0524.bingo.Bingo.cfg;
import static fun.mikolaj0524.bingo.Bingo.getPluginInstance;
import static fun.mikolaj0524.bingo.Lobby.TeleportToSpawn;
import static fun.mikolaj0524.bingo.MapManager.playerMaterials;
import static fun.mikolaj0524.bingo.Messages.*;


public class PlayerManager implements Listener {


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		e.setJoinMessage("\n" + getPrefix() + getFromConfig("messages.server_join").replaceAll("%player%", player.getDisplayName() + getColor()) + "\n");

		if (playerMaterials.containsKey(player)) {
			playerMaterials.remove(player);
		}

		playerMaterials.put(player, new ArrayList<>());
		player.sendTitle(ChatColor.GREEN + getFromConfig("messages.welcome"), "", 10, 20, 10);
		TeleportToSpawn(player);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage("\n" + getPrefix() + getFromConfig("messages.server_leave").replaceAll("%player%", player.getDisplayName() + getColor()) + "\n");

		playerMaterials.remove(player);

	}
}
