public class Planet {

    public int name;

    public int positionX;
    public int positionY;

    public float size;
    public int fleetSize;

    public Planet(String name, String positionX, String positionY, String size, String fleetSize) {
        this.name = Integer.parseInt(name);
        this.positionX = Integer.parseInt(positionX);
        this.positionY = Integer.parseInt(positionY);
        this.size = Float.parseFloat(size);
        this.fleetSize = Integer.parseInt(fleetSize);
    }
}
