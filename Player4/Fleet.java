import java.util.ArrayList;

public class Fleet{


    public static ArrayList<Fleet> fleets;


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


    public static void addNewFleet(String[] tokens){

        Fleet fleet = new Fleet(
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]),
                Integer.parseInt(tokens[3]),
                Integer.parseInt(tokens[4]),
                Integer.parseInt(tokens[5]),
                Integer.parseInt(tokens[6]),
                PlayerData.getPlayerByColor(tokens[7]));

        Fleet.fleets.add(fleet);
    }

    public int getNeededTurns(){
        return neededTurns - currentTurn;
    }

}
