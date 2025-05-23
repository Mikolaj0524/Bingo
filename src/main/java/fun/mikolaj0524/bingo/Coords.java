package fun.mikolaj0524.bingo;

public class Coords {
	public static int x;
	public static int y;
	public static int z;

	public Coords(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

	public int getZ(){
		return this.z;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setZ(int z){
		this.z = z;
	}
}
