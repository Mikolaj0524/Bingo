package fun.mikolaj0524.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static fun.mikolaj0524.bingo.Bingo.cfg;
import static fun.mikolaj0524.bingo.Bingo.getPluginInstance;
import static fun.mikolaj0524.bingo.GameLogic.gameType1;
import static fun.mikolaj0524.bingo.GameLogic.playerEndGame;

public class MapManager implements Listener {

	private static ArrayList<Material> randomItems = new ArrayList<>();
	public static HashMap<Player, ArrayList<ItemData>> playerMaterials = new HashMap<>();

	@EventHandler
	public void onItemPickup(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) return;

		Player player = (Player) event.getEntity();
		ItemStack item = event.getItem().getItemStack();

		checkMapForUpdates(player, item.getType());
	}

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

	public static void checkMapForUpdates(Player player, Material material){

		for(ItemData itemData : playerMaterials.get(player)){
			if(itemData.getMaterial() == material && !itemData.getState()){
				itemData.setState(true);
				updateCustomMap(player);
			}
		}
	}

	public static void loadItems() {
		if (randomItems.isEmpty()) {
			List<String> materialsFromConfig = cfg.getStringList("materials");
			for (String name : materialsFromConfig) {
				try {
					Material mat = Material.valueOf(name.toUpperCase());
					randomItems.add(mat);
				} catch (Exception ex){ }
			}
		}
	}

	public static void updateCustomMap(Player player) {
		ItemStack item = player.getInventory().getItem(40);
		if (item == null || item.getType() != Material.FILLED_MAP || !(item.getItemMeta() instanceof MapMeta)) {
			return;
		}

		MapMeta meta = (MapMeta) item.getItemMeta();
		MapView view = meta.getMapView();

		if (view == null) {
			return;
		}

		view.getRenderers().clear();
		view.addRenderer(new CustomMapRenderer());

		ArrayList<ItemData> listOfItems = playerMaterials.get(player);

		if(gameType1){
			int count = 0;
			for(ItemData itemData : listOfItems){
				if(itemData.getState()){
					count++;
				}
			}
			if(count >= 25){
				playerEndGame(player);
			}
		}
		else{
			ArrayList<ItemData> d = listOfItems;
			boolean done = false;
			for (int row = 0; row < 5; row++) { if (d.get(row * 5).getState() && d.get(row * 5 + 1).getState() && d.get(row * 5 + 2).getState() && d.get(row * 5 + 3).getState() && d.get(row * 5 + 4).getState()) { done = true; } }
			for (int col = 0; col < 5; col++) { if (d.get(col).getState() && d.get(col + 5).getState() && d.get(col + 10).getState() && d.get(col + 15).getState() && d.get(col + 20).getState()) { done = true; } }
			if (d.get(0).getState() && d.get(6).getState() && d.get(12).getState() && d.get(18).getState() && d.get(24).getState()) { done = true; }
			if (d.get(4).getState() && d.get(8).getState() && d.get(12).getState() && d.get(16).getState() && d.get(20).getState()) { done = true; }
			if(done){ playerEndGame(player); }
		}

	}

	public static void giveCustomMap(Player player) {
		playerMaterials.get(player).clear();
		player.getInventory().setItem(40, new ItemStack(Material.AIR, 1));
		ArrayList<ItemData> arrayList = playerMaterials.get(player);

		Random random = new Random();
		while (arrayList == null || arrayList.isEmpty() || arrayList.size() < 25) {
			Material randomMaterial = randomItems.get(random.nextInt(randomItems.size()));

			Boolean contains = false;
			for(ItemData itemData : arrayList){
				if(itemData.getMaterial() == randomMaterial){
					contains = true;
				}
			}
			if (!contains) {
				arrayList.add(new ItemData(randomMaterial, false));
			}
		}

		World world = player.getWorld();
		MapView mapView = Bukkit.createMap(world);
		mapView.getRenderers().clear();
		mapView.addRenderer(new CustomMapRenderer());

		ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
		MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
		if (mapMeta != null) {
			mapMeta.setMapView(mapView);
			mapItem.setItemMeta(mapMeta);
		}

		player.getInventory().setItem(40, mapItem);
	}


	private static class CustomMapRenderer extends MapRenderer {
		private boolean rendered = false;

		@Override
		public void render(MapView map, MapCanvas canvas, Player player) {
			if (rendered) return;
			rendered = true;

			try {
				BufferedImage resized = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = resized.createGraphics();

				g.setColor(Color.BLUE);
				g.drawRect(0, 0, 128, 128);
				g.fillRect(0, 0, 128, 128);

				g.setColor(Color.GRAY);
				g.setFont(new Font("Arial", Font.BOLD, 10));
				g.drawString("Bingo Card", 37, 10);

				g.setColor(Color.GRAY);
				int spacing = 2, border = 116, startX = 7, startY = 13;
				int boxWidth = (border - (spacing * 4)) / 5, boxHeight = (border - (spacing * 4)) / 5;

				int counter = 0;
				for (int x = 0; x < 5; x++) {
					for (int y = 0; y < 5; y++) {
						int drawX = startX + x * (boxWidth + spacing), drawY = startY + y * (boxHeight + spacing);
						g.drawRect(drawX, drawY, boxWidth, boxHeight);
						g.fillRect(drawX, drawY, boxWidth, boxHeight);

						String path = getPluginInstance().getDataFolder().getAbsolutePath();
						String itemName = playerMaterials.get(player).get(counter).getMaterial().toString().replace("Material.", "");
						String folderPath = path + "\\images\\" + itemName + ".png";
						File itemImage = new File(folderPath);
						if (itemImage.exists()) {
							BufferedImage image = ImageIO.read(itemImage);
							g.drawImage(image, drawX + (boxWidth / 2) - 8 + 1, drawY + (boxHeight / 2) - 8 + 1, 16, 16, null);

							if(playerMaterials.get(player).get(counter).getState()){
								image = ImageIO.read(new File(path + "\\images\\tick.png"));
								g.drawImage(image, drawX + (boxWidth / 2) - 8 + 1, drawY + (boxHeight / 2) - 8 + 1, 16, 16, null);
							}

						}
						else {
							player.sendMessage("Image for " + itemName + " not found.");
						}
						counter++;
					}
				}
				g.dispose();
				canvas.drawImage(0, 0, resized);
			} catch (IOException e) {
				player.sendMessage("Error: " + e);
			}
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		if (player.getInventory().getItemInOffHand().equals(item)) {
			event.setCancelled(true);
		}
	}

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

