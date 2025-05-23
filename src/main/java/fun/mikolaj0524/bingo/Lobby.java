package fun.mikolaj0524.bingo;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static fun.mikolaj0524.bingo.GameLogic.*;
import static fun.mikolaj0524.bingo.MapGenerator.buildSpawn;
import static fun.mikolaj0524.bingo.MapGenerator.resetWorld;
import static fun.mikolaj0524.bingo.MapManager.giveCustomMap;
import static fun.mikolaj0524.bingo.Messages.getColor;
import static fun.mikolaj0524.bingo.Messages.getFromConfig;

public class Lobby implements Listener {
	private static HashMap<String, Location> signPositions = new HashMap<>();
	public static boolean gameType = true;
	public static ArrayList<Player> queue = new ArrayList<>();
	private static Location spawnPos = new Location(Bukkit.getWorld("world"), 0, 101, 0, 90, 0);

	public static void loadSings(){
		World world = Bukkit.getWorld("world");
		signPositions.put("game", new Location(world, -9, 102, 0));
		signPositions.put("start", new Location(world, -9, 101, 0));
		signPositions.put("type", new Location(world, -9, 103, 0));
	}

	public static void createSigns(){
		World world = Bukkit.getWorld("world");

		if(world != null){
			Block block = world.getBlockAt(signPositions.get("game"));
			block.setType(Material.OAK_WALL_SIGN);
			WallSign wallSignData = (WallSign) block.getBlockData();
			wallSignData.setFacing(BlockFace.EAST);
			block.setBlockData(wallSignData);
			BlockState state = block.getState();
			if (state instanceof Sign sign) {
				SignSide side = sign.getSide(Side.FRONT);
				side.setLine(1, ChatColor.GREEN + "" + ChatColor.BOLD + "[Game]");
				side.setLine(2, ChatColor.WHITE + "0 / 8");
				sign.update();
			}

			block = world.getBlockAt(signPositions.get("start"));
			block.setType(Material.OAK_WALL_SIGN);
			wallSignData = (WallSign) block.getBlockData();
			wallSignData.setFacing(BlockFace.EAST);
			block.setBlockData(wallSignData);
			state = block.getState();
			if (state instanceof Sign sign) {
				SignSide side = sign.getSide(Side.FRONT);
				side.setLine(1, ChatColor.GREEN + "Start");
				sign.update();
			}

			block = world.getBlockAt(signPositions.get("type"));
			block.setType(Material.OAK_WALL_SIGN);
			wallSignData = (WallSign) block.getBlockData();
			wallSignData.setFacing(BlockFace.EAST);
			block.setBlockData(wallSignData);
			state = block.getState();
			if (state instanceof Sign sign) {
				SignSide side = sign.getSide(Side.FRONT);
				side.setLine(1, ChatColor.GREEN + "[Type]");
				side.setLine(2, ChatColor.WHITE + "Full house");
				sign.update();
			}



		}

	}

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

	public static void refreshGameSign(){
		Sign sign = (Sign) Bukkit.getWorld("world").getBlockAt(signPositions.get("game")).getState();
		sign.getSide(Side.FRONT).setLine(2, ChatColor.WHITE + "" + queue.size() + " / 8");
		sign.update(true);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();

			if(player.getWorld() == Bukkit.getWorld("world") || player.getGameMode() == GameMode.ADVENTURE){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		PreparePlayer(player);
		player.setGameMode(GameMode.ADVENTURE);
		player.setSaturation(20);
	}

	public static void TeleportToSpawn(Player player){
		player.teleport(spawnPos);
		player.setSaturation(20);
		PreparePlayer(player);
	}

	public static void TeleportToMap(Player player){
		World world = Bukkit.getWorld("bingoworld");
		Random random = new Random();

		int width = Integer.parseInt(getFromConfig("map.width"));
		int height = Integer.parseInt(getFromConfig("map.height"));
		width /= 2;
		height /= 2;
		width -= 30;
		height -= 30;

		int x = random.nextInt(-width, width);
		int z = random.nextInt(-height, height);
		int y = world.getHighestBlockYAt(x, z);

		while (world.getBlockAt(new Location(world, x, y, z)).getType() == Material.WATER){
			x = random.nextInt(-width, width);
			z = random.nextInt(-height, height);
			y = world.getHighestBlockYAt(x, z);
		}

		y += 30;

		buildSpawn(x, y, z);
		Location location = new Location(world, x + 0.5, y, z + 0.5, 90, 0);
		PreparePlayer(player);
		player.teleport(location);
		player.removePotionEffect(PotionEffectType.REGENERATION);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 25, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1));
	}

	public static void PreparePlayer(Player player){
		player.setHealth(20.0);
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
		player.setExp(0);
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
		player.setGameMode(GameMode.ADVENTURE);
		player.setFoodLevel(20);
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 9));
		player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.0f);
	}

}
