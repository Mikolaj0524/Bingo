package fun.mikolaj0524.bingo.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import static fun.mikolaj0524.bingo.Elements.MapManager.checkMapForUpdates;

public class ItemPickup implements Listener {

	@EventHandler
	public void onItemPickup(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) return;

		Player player = (Player) event.getEntity();
		ItemStack item = event.getItem().getItemStack();

		checkMapForUpdates(player, item.getType());
	}

}
