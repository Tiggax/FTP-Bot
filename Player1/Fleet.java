

public class Fleet {


    public int name;

    public int size;

    public Planet originPlanet;
    public Planet destinationPlanet;

    public int currentTurn;
    public int neededTurns;

    public PlayerData player;

    public int turn;

    public Fleet(String name, String size, String originPlanet, String destinationPlanet, String currentTurn, String neededTurns, PlayerData player) {

        this.name = Integer.parseInt(name);
        this.size = Integer.parseInt(size);

        this.originPlanet = PlayerData.findPlanetByName(Integer.parseInt(originPlanet));
        this.destinationPlanet = PlayerData.findPlanetByName(Integer.parseInt(destinationPlanet));

        this.currentTurn = Integer.parseInt(currentTurn);
        this.neededTurns = Integer.parseInt(neededTurns);

        this.player = player;

    }
}
