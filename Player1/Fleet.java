import java.util.ArrayList;

public class Fleet {

    public static ArrayList<Fleet> allFleets;

    public int name;

    public int size;

    public Planet originPlanet;
    public Planet destinationPlanet;

    public int currentTurn;
    public int neededTurns;

    public int turn;

    public Fleet(String name, String size, String originPlanet, String destinationPlanet, String currentTurn, String neededTurns) {

        this.name = Integer.parseInt(name);
        this.size = Integer.parseInt(size);

        this.originPlanet = Planet.findPlanetByName(Integer.parseInt(originPlanet));
        this.destinationPlanet = Planet.findPlanetByName(Integer.parseInt(destinationPlanet));

        this.currentTurn = Integer.parseInt(currentTurn);
        this.neededTurns = Integer.parseInt(neededTurns);

    }
}
