package fun.mikolaj0524.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static fun.mikolaj0524.bingo.Bingo.cfg;
import static fun.mikolaj0524.bingo.GameLogic.inGamePlayers;
import static fun.mikolaj0524.bingo.Lobby.queue;

public class Messages {

	public static String getFromConfig(String path){
		String message = cfg.getString(path);
		if(message != null && !message.isEmpty()){
			return ChatColor.translateAlternateColorCodes('&', message);
		}
		return "";
	}

	public static String getPrefix(){
		return getFromConfig("messages.prefix");
	}

	public static void toPlayer(Player player, String message){
		player.sendMessage("\n" + getPrefix() + message + "\n ");
	}

	public static String getColor(){
		return ChatColor.translateAlternateColorCodes('&', getFromConfig("messages.color"));
	}

	public static void toOnlinePlayers(String message){
		for(Player p : Bukkit.getOnlinePlayers()){
			toPlayer(p, message);
		}
	}

	public static void toQueuePlayers(String message){
		for(Player p : queue){
			toPlayer(p, message);
		}
	}

	public static void toInGamePlayers(String message){
		for(Player p : inGamePlayers){
			toPlayer(p, message);
		}
	}

	public static void displayTitle(ChatColor color, String message){
		for(Player p : inGamePlayers){
			p.sendTitle(color + message, "", 10, 25, 10);
		}
	}


}
