package fun.mikolaj0524.bingo.Elements;

import jdk.jfr.Timespan;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static fun.mikolaj0524.bingo.Bingo.getPluginInstance;
import static fun.mikolaj0524.bingo.Elements.MapGenerator.buildSpawn;
import static fun.mikolaj0524.bingo.Elements.Messages.getFromConfig;

public class Lobby implements Listener {
	public static HashMap<String, Location> signPositions = new HashMap<>();
	public static boolean gameType = true;
	public static ArrayList<Player> queue = new ArrayList<>();
	public static Location spawnPos;

	public static void loadLocations(){
		World world = Bukkit.getWorld("world");
		Configuration cfg = getPluginInstance().getConfig();
		spawnPos = new Location(world, cfg.getDouble("spawn.x"), cfg.getDouble("spawn.y"), cfg.getDouble("spawn.z"), cfg.getInt("spawn.yaw"), 0);
		signPositions.put("game", new Location(world, cfg.getDouble("signs.game.x"), cfg.getDouble("signs.game.y"), cfg.getDouble("signs.game.z")));
		signPositions.put("start", new Location(world, cfg.getDouble("signs.start.x"), cfg.getDouble("signs.start.y"), cfg.getDouble("signs.start.z")));
		signPositions.put("type", new Location(world, cfg.getDouble("signs.type.x"), cfg.getDouble("signs.type.y"), cfg.getDouble("signs.type.z")));
	}

	public static void createSigns(){
		World world = Bukkit.getWorld("world");
		Configuration cfg = getPluginInstance().getConfig();

		if(world != null){
			Block block = world.getBlockAt(signPositions.get("game"));
			block.setType(Material.OAK_WALL_SIGN);
			WallSign wallSignData = (WallSign) block.getBlockData();
			wallSignData.setFacing(BlockFace.valueOf(cfg.getString("signs.game.facing")));
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
			wallSignData.setFacing(BlockFace.valueOf(cfg.getString("signs.start.facing")));
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
			wallSignData.setFacing(BlockFace.valueOf(cfg.getString("signs.type.facing")));
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

	public static void refreshGameSign(){
		Sign sign = (Sign) Bukkit.getWorld("world").getBlockAt(signPositions.get("game")).getState();
		sign.getSide(Side.FRONT).setLine(2, ChatColor.WHITE + "" + queue.size() + " / 8");
		sign.update(true);
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
