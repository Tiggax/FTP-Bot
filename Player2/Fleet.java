public class Fleet {

    public int name;

    public int size;

    public int originPlanet;
    public int destinationPlanet;

    public int currentTurn;
    public int neededTurns;

    public Fleet(String name, String size, String originPlanet, String destinationPlanet, String currentTurn, String neededTurns) {
        this.name = Integer.parseInt(name);
        this.size = Integer.parseInt(size);
        this.originPlanet = Integer.parseInt(originPlanet);
        this.destinationPlanet = Integer.parseInt(destinationPlanet);
        this.currentTurn = Integer.parseInt(currentTurn);
        this.neededTurns = Integer.parseInt(neededTurns);
    }
}
