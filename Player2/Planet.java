import java.util.ArrayList;

public class Planet implements Cloneable {


    public static ArrayList<Planet> planets;

    public static final int speed = 2;
    public static final int fleetSizeIncreaseByTurn = 10;


    public int name;

    public int positionX;
    public int positionY;

    public float size;
    public int fleetSize;

    public Players player;

    private static final int[] playersPlanetFleetCount = new int[Players.values().length];
    private static final float[] playersPlanetFleetSize = new float[Players.values().length];

    public Planet(int name, int positionX, int positionY, float size, int fleetSize, Players player) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;
        this.fleetSize = fleetSize;
        this.player = player;
    }


    public static int getPlayerPlanetCount(Players player){
        return playersPlanetFleetCount[player.ordinal()];
    }

    public static float getPlayerPlanetSize(Players player){
        return playersPlanetFleetSize[player.ordinal()];
    }


    public static void addNewPlanet(String[] tokens){
        Planet planet = new Planet(
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]),
                Integer.parseInt(tokens[3]),
                Float.parseFloat(tokens[4]),
                Integer.parseInt(tokens[5]),
                PlayerData.getPlayerByColor(tokens[6]));

        ++playersPlanetFleetCount[planet.player.ordinal()];
        playersPlanetFleetSize[planet.player.ordinal()] += planet.size;

        Planet.planets.add(planet);
    }

    public int turnDistance(Planet planet){
        return (int)(Math.sqrt((positionX - planet.positionX) *
                               (positionX - planet.positionX) +
                               (positionY - planet.positionY) *
                               (positionY - planet.positionY)
        )) / speed;
    }

    public int getFleetSize(int turns){
        return (int)(turns * fleetSizeIncreaseByTurn * size + fleetSize);
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
