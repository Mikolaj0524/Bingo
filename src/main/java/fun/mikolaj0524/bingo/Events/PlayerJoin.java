package fun.mikolaj0524.bingo.Events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

import static fun.mikolaj0524.bingo.Elements.MapManager.playerMaterials;
import static fun.mikolaj0524.bingo.Elements.Lobby.PreparePlayer;
import static fun.mikolaj0524.bingo.Elements.Lobby.TeleportToSpawn;
import static fun.mikolaj0524.bingo.Elements.Messages.*;
import static fun.mikolaj0524.bingo.Elements.Messages.getFromConfig;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		PreparePlayer(player);
		player.setGameMode(GameMode.ADVENTURE);
		player.setSaturation(20);

		e.setJoinMessage("\n" + getPrefix() + getFromConfig("messages.server_join").replaceAll("%player%", player.getDisplayName() + getColor()) + "\n");

		playerMaterials.remove(player);

		playerMaterials.put(player, new ArrayList<>());
		player.sendTitle(ChatColor.GREEN + getFromConfig("messages.welcome"), "", 10, 20, 10);
		TeleportToSpawn(player);
	}

}
