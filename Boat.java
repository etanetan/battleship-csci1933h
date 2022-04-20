public class Boat {
    private int size;
    // true means it is horizontal, false means it is vertical
    private boolean direction;
    private Cell[] location;

    public Boat(int size, boolean direction, Cell[] location) {
        this.size = size;
        this.direction = direction;
        this.location = location;
    }

    public int getSize() {
        return size;
    }

    public boolean getDirection() {
        return direction;
    }

    public Cell[] getLocation() {
        return location;
    }

    public void setSize(int s) {
        size = s;
    }

    public void setDirection(boolean b) {
        direction = b;
    }
}
