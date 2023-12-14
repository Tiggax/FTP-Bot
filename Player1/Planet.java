

public class Planet implements Cloneable {

    public int name;

    public int positionX;
    public int positionY;

    public float size;
    public int fleetSize;

    public PlayerData player;

    public Planet(String name, String positionX, String positionY, String size, String fleetSize, PlayerData player) {
        this.name = Integer.parseInt(name);
        this.positionX = Integer.parseInt(positionX);
        this.positionY = Integer.parseInt(positionY);
        this.size = Float.parseFloat(size);
        this.fleetSize = Integer.parseInt(fleetSize);
        this.player = player;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
