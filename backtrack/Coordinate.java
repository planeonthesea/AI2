public class Coordinate { 
	// A little counter-intuitive, but x represents row and y represents column
	public final int x; 
	public final int y; 
	public Coordinate(int row, int col) { 
		this.x = row; 
		this.y = col; 
	}

	public String toString() {
		return "[" + this.x + ", " + this.y + "]";
	}

	public boolean equals(Object o) {
		if (!(o instanceof Coordinate)) {
			return false;
		}
		boolean equal = true;
		Coordinate coord = (Coordinate) o;

		if (this.x != coord.x || this.y != coord.y) {
			equal = false;
		}

		return equal;
	}

	public int hashCode() {
		String hashStr = "" + this.x + this.y;
		return hashStr.hashCode();
	}
}