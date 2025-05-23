package fun.mikolaj0524.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static fun.mikolaj0524.bingo.Bingo.cfg;
import static fun.mikolaj0524.bingo.Bingo.getPluginInstance;
import static fun.mikolaj0524.bingo.Lobby.*;
import static fun.mikolaj0524.bingo.MapGenerator.destroySpawns;
import static fun.mikolaj0524.bingo.MapManager.giveCustomMap;
import static fun.mikolaj0524.bingo.Messages.*;

public class GameLogic implements Listener {
	public static int time;
	public static boolean gameState = false;
	public static boolean gameType1 = false;
	public static ArrayList<Player> inGamePlayers = new ArrayList<>();

	public static void setupLogic(){
		new BukkitRunnable() {
			@Override
			public void run() {
				if(gameState){
					time++;
					if(time == 5){
						displayTitle(ChatColor.DARK_RED , "5");
						for(Player p : inGamePlayers){
							giveCustomMap(p);
						}
					}
					else if(time == 7){
						displayTitle(ChatColor.RED , "4");
					}
					else if(time == 9){
						displayTitle(ChatColor.GOLD , "3");
					}
					else if(time == 11){
						displayTitle(ChatColor.YELLOW , "2");
					}
					else if(time == 13){
						displayTitle(ChatColor.GREEN , getFromConfig("messages.start_text"));
						destroySpawns();
						for(Player p : inGamePlayers){
							p.setGameMode(GameMode.SURVIVAL);
						}
					}

				}
				else{
					time = 0;
				}

				if(inGamePlayers.isEmpty()){
					gameState = false;
				}
			}
		}.runTaskTimer(getPluginInstance(), 0, 20);
	}

	public static void playerEndGame(Player player){
		inGamePlayers.remove(player);
		PreparePlayer(player);
		TeleportToSpawn(player);
		Messages.toOnlinePlayers(getFromConfig("messages.player_completed_bingo").replaceAll("%player%", player.getDisplayName() + getColor()));
		gameEnd();
	}

	public static void gameEnd(){
		for(Player player : Bukkit.getOnlinePlayers()){
			TeleportToSpawn(player);
			player.setGameMode(GameMode.ADVENTURE);

			inGamePlayers.clear();
		}
	}


	public static void gameStart(){
		time = 0;
		gameState = true;
		gameType1 = gameType;
		inGamePlayers.clear();
		for(Player p : queue){
			inGamePlayers.add(p);
			TeleportToMap(p);
			Messages.toPlayer(p, getFromConfig("messages.teleporting"));
			p.setGameMode(GameMode.ADVENTURE);
		}
		queue.clear();
		refreshGameSign();
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
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


	public static void CheckEnd(){
		if(inGamePlayers.size() == 1){
			Player player = inGamePlayers.getFirst();
			inGamePlayers.remove(player);
			TeleportToSpawn(player);
			player.setGameMode(GameMode.ADVENTURE);
			inGamePlayers.clear();
			Messages.toOnlinePlayers(getFromConfig("messages.someone_won").replaceAll("%player%", player.getDisplayName() + getColor()));

		}
		else if(inGamePlayers.isEmpty()){
			Messages.toOnlinePlayers(getFromConfig("messages.game_end"));
		}

	}

}
