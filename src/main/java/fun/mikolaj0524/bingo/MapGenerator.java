package fun.mikolaj0524.bingo;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.util.ArrayList;

import static fun.mikolaj0524.bingo.Messages.getFromConfig;

public class MapGenerator implements Listener {

	private static ArrayList<Coords> spawns = new ArrayList<>();

	public static void resetWorld() {
		String worldName = "bingoworld";
		World world = Bukkit.getWorld(worldName);

		if (world != null) {
			world.setAutoSave(false);
			world.save();

			boolean unloaded = Bukkit.unloadWorld(world, false);
			if (!unloaded) {
				Bukkit.getLogger().warning("Failed to unload world: " + worldName);
				return;
			}

			File worldFolder = new File(Bukkit.getWorldContainer(), worldName);

			if(worldFolder.isDirectory()){
				worldFolder.delete();
			}

		}

		Bukkit.createWorld(new WorldCreator(worldName));
		Bukkit.getLogger().info("World '" + worldName + "' has been reset.");
	}


	public static void teleportNewWorld(Player player){
		if(player.isOp()){
			Messages.toQueuePlayers(getFromConfig("messages.teleporting"));
			if(player.getWorld().getName().equals("world")){
				player.teleport(new Location(Bukkit.getWorld("bingoworld"), 0, 150, 0));
			}
			else{
				player.teleport(new Location(Bukkit.getWorld("world"), 0, 150, 0));
			}

		}
		else{
			Messages.toPlayer(player, getFromConfig("messages.no_perms"));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 1.0f);
		}
	}

	public static void buildSpawn(int x, int y, int z){
		World world = Bukkit.getWorld("bingoworld");

		for (int ix = x - 1; ix <= x + 1; ix++) {
			for (int iy = y - 1; iy <= y + 2; iy++) {
				for (int iz = z - 1; iz <= z + 1; iz++) {
					if ((ix == x && iy == y && iz == z) || (ix == x && iy == y + 1 && iz == z))
						continue;

					world.getBlockAt(ix, iy, iz).setType(Material.GLASS);
				}
			}
		}

		spawns.add(new Coords(x, y, z));
	}

	public static void destroySpawns(){
		World world = Bukkit.getWorld("bingoworld");

		for(Coords spawn : spawns){
			int x1 = spawn.getX() - 1;
			int y1 = spawn.getY() - 1;
			int z1 = spawn.getZ() -1;

			int x2 = spawn.getX() + 1;
			int y2 = spawn.getY() + 2;
			int z2 = spawn.getZ() + 1;

			int minX = (int) Math.min(x1, x2) - 1;
			int minY = (int) Math.min(y1, y2) - 1;
			int minZ = (int) Math.min(z1, z2) - 1;
			int maxX = (int) Math.max(x1, x2) + 1;
			int maxY = (int) Math.max(y1, y2) + 2;
			int maxZ = (int) Math.max(z1, z2) + 1;

			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					for (int z = minZ; z <= maxZ; z++) {
						world.getBlockAt(x, y, z).setType(Material.AIR);
					}
				}
			}
		}
		spawns.clear();
	}

}
