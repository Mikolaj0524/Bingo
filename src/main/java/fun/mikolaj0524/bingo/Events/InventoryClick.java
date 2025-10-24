package fun.mikolaj0524.bingo.Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static fun.mikolaj0524.bingo.Elements.MapManager.checkMapForUpdates;

public class InventoryClick implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player player)) return;

		if (event.getSlot() == 40) {
			ItemStack item = event.getCurrentItem();
			if (item != null && item.getType() == Material.FILLED_MAP) {
				event.setCancelled(true);
			}
		}

		InventoryAction action = event.getAction();
		ItemStack itemStack = event.getCurrentItem();
		if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY || action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_SOME || action == InventoryAction.PLACE_ONE) {
			checkMapForUpdates(player, itemStack.getType());
		}
	}

}
