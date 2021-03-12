import java.awt.*;

public class EdableObject {

    private Point location;

    public EdableObject(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
