public class Settings {
    
    private int speed, size;

    public Settings(int speed, int sizeFactor, int bodySize) 
    {
        this.speed = speed;
        this.setSize(sizeFactor, bodySize);
    }
    public void setSize(int sizeFactor, int bodySize) {
        this.size = bodySize * sizeFactor;
        // System.err.println(this.size);
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getSize() {
        return this.size;
    }
    public int getSpeed()
    {
        return this.speed;
    }
}
