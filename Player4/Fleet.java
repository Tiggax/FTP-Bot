
public class Fleet{

    public int name;

    public int size;

    public int originPlanet;
    public int destinationPlanet;

    public int currentTurn;
    public int neededTurns;

    public Players player;


    public Fleet(int name, int size, int originPlanet, int destinationPlanet, int currentTurn, int neededTurns, Players player) {

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
