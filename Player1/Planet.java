import java.util.ArrayList;

public class Planet implements Cloneable {


    public static ArrayList<Planet> planets;

    public static final int speed = 2;

    public int name;

    public int positionX;
    public int positionY;

    public float size;
    public int fleetSize;

    public Players player;

    public Planet(int name, int positionX, int positionY, float size, int fleetSize, Players player) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;
        this.fleetSize = fleetSize;
        this.player = player;
    }



    public static void addNewPlanet(String[] tokens){
        Planet planet = new Planet(
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]),
                Integer.parseInt(tokens[3]),
                Float.parseFloat(tokens[4]),
                Integer.parseInt(tokens[5]),
                PlayerData.getPlayerByColor(tokens[6]));

        Planet.planets.add(planet);
    }

    public int turnDistance(Planet planet){
        return (int)(Math.sqrt((positionX - planet.positionX) *
                               (positionX - planet.positionX) +
                               (positionY - planet.positionY) *
                               (positionY - planet.positionY)
        )) / speed;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
