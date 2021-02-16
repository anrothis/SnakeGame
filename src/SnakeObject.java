import java.awt.*;

public class SnakeObject {
    
    private Point position;
    private Point direction;

    public SnakeObject(int startingPos, int stepSize)
    {
        position = new Point(startingPos,startingPos);
        direction = new Point(0,-stepSize);
    }
    public SnakeObject(Point pos) {
        this.position = pos;
    }
    public void setPostion(Point p) {
        this.position = p;
    }
    public void movePosition() 
    {
        this.position.translate(direction.x, direction.y);
    }
    public Point getPosition()
    {
        return this.position;
    }
    public void setDirection(Point dPoint) {
        this.direction = dPoint;
    }
    public Point getDirection()
    {
        return this.direction;
    }

}
