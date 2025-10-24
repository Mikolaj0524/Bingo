package fun.mikolaj0524.bingo.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static fun.mikolaj0524.bingo.Elements.MapManager.playerMaterials;
import static fun.mikolaj0524.bingo.Elements.Messages.*;

public class PlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage("\n" + getPrefix() + getFromConfig("messages.server_leave").replaceAll("%player%", player.getDisplayName() + getColor()) + "\n");

		playerMaterials.remove(player);
	}

}
