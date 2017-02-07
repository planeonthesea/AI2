public class Coordinate { 
    public final int x; 
    public final int y; 
    public Coordinate(int x, int y) { 
        this.x = x; 
        this.y = y; 
    }

    public String toString() {
        return "Row: " + this.x + "; Col: " + this.y;
    }

    public Boolean equals(Coordinate coord) {
        Boolean equal = true;

        if (this.x != coord.x || this.y != coord.y) {
            equal = false;
        }

        return equal;
    }
}