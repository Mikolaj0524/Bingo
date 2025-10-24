package fun.mikolaj0524.bingo.Events;

import fun.mikolaj0524.bingo.Elements.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static fun.mikolaj0524.bingo.Elements.GameLogic.gameStart;
import static fun.mikolaj0524.bingo.Elements.MapGenerator.resetWorld;
import static fun.mikolaj0524.bingo.Elements.Messages.getFromConfig;
import static fun.mikolaj0524.bingo.Elements.Lobby.*;

public class PlayerRightClickSign implements Listener {

	@EventHandler
	public void onPlayerRightClickSign(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				Player player = event.getPlayer();

				if (sign.getBlock().getLocation().equals(signPositions.get("type"))) {
					if(gameType){
						gameType = false;
						sign.getSide(Side.FRONT).setLine(2, ChatColor.WHITE + "Line");
					}
					else {
						gameType = true;
						sign.getSide(Side.FRONT).setLine(2, ChatColor.WHITE + "Full house");
					}
					sign.update(true);
					player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
				}

				if (sign.getBlock().getLocation().equals(signPositions.get("game"))) {
					if(queue.contains(player)){
						queue.remove(player);
						Messages.toPlayer(player, getFromConfig("messages.queue_leave"));
					}
					else{
						queue.add(player);
						Messages.toPlayer(player, getFromConfig("messages.queue_join"));

					}
					sign.getSide(Side.FRONT).setLine(2, ChatColor.WHITE + "" + queue.size() + " / 8");
					sign.update(true);
					player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
				}

				if (sign.getBlock().getLocation().equals(signPositions.get("start"))) {
					if(player.hasPermission("bingo.start")){
						if(queue.size() >= 2){
							Messages.toQueuePlayers(getFromConfig("messages.starting"));
							player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);

							resetWorld();
							gameStart();
						}
						else{
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 1.0f);
							Messages.toPlayer(player, getFromConfig("messages.not_enough_players"));
						}
					}
					else{
						Messages.toPlayer(player, getFromConfig("messages.no_perms"));
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 1.0f);
					}
				}

			}
		}
	}

}
