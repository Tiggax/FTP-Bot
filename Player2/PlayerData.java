import java.util.ArrayList;


enum Players{

    NEUTRAL,
    ME,
    TEAMMATE,
    FIRST_ENEMY,
    SECOND_ENEMY

}

public class PlayerData {


    public static String[] possibleColors = {"blue", "cyan", "green", "yellow", "null"};





    public String color = "";


    public ArrayList<Planet> planets;
    public ArrayList<Fleet> fleets;

    public void resetData() {
        planets = new ArrayList<>();
        fleets = new ArrayList<>();
        Planet.planetsOfAllPlayers = new ArrayList<>();
        Fleet.fleetsOfAllPlayers = new ArrayList<>();
    }

    public PlayerData(boolean neutral) {
        if (neutral) color = possibleColors[possibleColors.length - 1];
    }

    public void addNewPlanet(String[] tokens){
        Planet planet = new Planet(
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]),
                Integer.parseInt(tokens[3]),
                Float.parseFloat(tokens[4]),
                Integer.parseInt(tokens[5]),
                this);

        planets.add(planet);
        Planet.planetsOfAllPlayers.add(planet);
    }

    public void addNewFleet(String[] tokens){

        Fleet fleet = new Fleet(
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]),
                Planet.findPlanetByName(Integer.parseInt(tokens[3])),
                Planet.findPlanetByName(Integer.parseInt(tokens[4])),
                Integer.parseInt(tokens[5]),
                Integer.parseInt(tokens[6]),
                this);

        fleets.add(fleet);
        Fleet.fleetsOfAllPlayers.add(fleet);
    }





    public boolean isInMyTeam(){
        return this == Player.player || this == Player.teammate;
    }

}
