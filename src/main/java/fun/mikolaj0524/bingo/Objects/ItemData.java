package fun.mikolaj0524.bingo.Objects;

import org.bukkit.Material;

public class ItemData {
	//private Integer id;
	private Material material;
	private Boolean state;

	public ItemData(/*Integer id, */Material material, Boolean state) {
		//this.id = id;
		this.material = material;
		this.state = state;
	}

	// Getters and setters

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}
}
