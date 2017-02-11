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

    public Boolean equals(Coordinate coord) {
        Boolean equal = true;

        if (this.x != coord.x || this.y != coord.y) {
            equal = false;
        }

        return equal;
    }
}