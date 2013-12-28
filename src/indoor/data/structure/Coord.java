package indoor.data.structure;

public class Coord {

	public static void main(String[] argArray) {
		System.out.println("Point in 3d world");
	}


	public final int x, y, z;


	public Coord(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object object) {
		Coord coord = null;
		if (object instanceof Coord) {
			coord = (Coord) object;
		}
		
		if (coord == null) {
			return false;
		}
		else if (this.x == coord.x && this.y == coord.y && this.z == coord.z) {
			return true;
		}
		else {
			return false;
		}
	}

}