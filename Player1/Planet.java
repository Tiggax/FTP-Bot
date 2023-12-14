

public class Planet implements Cloneable {

    public int name;

    public int positionX;
    public int positionY;

    public float size;
    public int fleetSize;

    public PlayerData player;

    public Planet(int name, int positionX, int positionY, float size, int fleetSize, PlayerData player) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;
        this.fleetSize = fleetSize;
        this.player = player;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
