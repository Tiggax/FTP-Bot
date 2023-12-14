

public class Fleet{


    public int name;

    public int size;

    public Planet originPlanet;
    public Planet destinationPlanet;

    public int currentTurn;
    public int neededTurns;

    public PlayerData player;

    public int turn;

    public Fleet(int name, int size, Planet originPlanet, Planet destinationPlanet, int currentTurn, int neededTurns, PlayerData player) {

        this.name = name;
        this.size = size;

        this.originPlanet = originPlanet;
        this.destinationPlanet = destinationPlanet;

        this.currentTurn = currentTurn;
        this.neededTurns = neededTurns;

        this.player = player;

    }

    public int getNeededTurns(){
        return neededTurns - currentTurn;
    }

}
