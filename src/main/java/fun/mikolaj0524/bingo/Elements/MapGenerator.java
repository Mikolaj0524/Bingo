package fun.mikolaj0524.bingo.Elements;

import fun.mikolaj0524.bingo.Objects.Coords;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;

import static fun.mikolaj0524.bingo.Elements.Messages.getFromConfig;

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
			int z1 = spawn.getZ() - 1;

			int x2 = spawn.getX() + 1;
			int y2 = spawn.getY() + 2;
			int z2 = spawn.getZ() + 1;

			int minX = Math.min(x1, x2);
			int minY = Math.min(y1, y2);
			int minZ = Math.min(z1, z2);
			int maxX = Math.max(x1, x2);
			int maxY = Math.max(y1, y2);
			int maxZ = Math.max(z1, z2);

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
