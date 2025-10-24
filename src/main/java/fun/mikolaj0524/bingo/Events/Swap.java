package fun.mikolaj0524.bingo.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class Swap implements Listener {

	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent event) {
		ItemStack offHand = event.getOffHandItem();
		ItemStack mainHand = event.getMainHandItem();
		if ((offHand != null && offHand.getType() == Material.FILLED_MAP)
				|| (mainHand != null && mainHand.getType() == Material.FILLED_MAP)) {
			event.setCancelled(true);
		}
	}

}
