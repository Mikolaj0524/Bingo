package fun.mikolaj0524.bingo.Events;

import fun.mikolaj0524.bingo.Elements.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static fun.mikolaj0524.bingo.Elements.GameLogic.CheckEnd;
import static fun.mikolaj0524.bingo.Elements.GameLogic.inGamePlayers;
import static fun.mikolaj0524.bingo.Elements.Lobby.PreparePlayer;
import static fun.mikolaj0524.bingo.Elements.Lobby.TeleportToSpawn;
import static fun.mikolaj0524.bingo.Elements.Messages.getColor;
import static fun.mikolaj0524.bingo.Elements.Messages.getFromConfig;

public class PlayerDamage implements Listener {
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){

		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();

			if(player.getWorld() == Bukkit.getWorld("world") || player.getGameMode() == GameMode.ADVENTURE){
				event.setCancelled(true);
			}

			double finalHealth = player.getHealth() - event.getFinalDamage();

			if(finalHealth <= 0){
				inGamePlayers.remove(player);
				PreparePlayer(player);
				TeleportToSpawn(player);
				Messages.toOnlinePlayers(getFromConfig("messages.player_died").replaceAll("%player%", player.getDisplayName() + getColor()));

				CheckEnd();

				event.setCancelled(true);
			}
		}
	}

}
