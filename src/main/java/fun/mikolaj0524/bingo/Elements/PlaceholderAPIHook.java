package fun.mikolaj0524.bingo.Elements;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static fun.mikolaj0524.bingo.Elements.GameLogic.time;
import static fun.mikolaj0524.bingo.Elements.Lobby.gameType;
import static fun.mikolaj0524.bingo.Elements.Lobby.queue;

public class PlaceholderAPIHook extends PlaceholderExpansion {
	@Override
	public @NotNull String getIdentifier() {
		return "bingo";
	}

	@Override
	public @NotNull String getAuthor() {
		return "Mikolaj0524";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.0.0";
	}

	public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {

		if(offlinePlayer != null && offlinePlayer.isOnline()){
			Player player = offlinePlayer.getPlayer();
			if(params.equalsIgnoreCase("cardtype")){
				if(gameType){
					return "Full house";
				}
				return "Line";
			}
			if(params.equalsIgnoreCase("gametime")){
				String minutes = String.valueOf(time / 60);
				String seconds = String.valueOf(time % 60);

				if(minutes.length() == 1){
					minutes = "0" + minutes;
				}
				if(seconds.length() == 1){
					seconds = "0" + seconds;
				}

				return minutes + ":" + seconds;

			}
			if(params.equalsIgnoreCase("queue")){
				return "" + queue.size();
			}
		}
		return null;
	}

	public static void registerHook(){
		new PlaceholderAPIHook().register();
	}
}
