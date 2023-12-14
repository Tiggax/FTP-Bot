import java.util.ArrayList;
import java.util.Comparator;

public class PlayerData {

    public static String[] possibleColors = {"blue", "cyan", "green", "yellow", "null"};

    public String color = "";

    public static ArrayList<Planet> planetsOfAllPlayers;
    public static ArrayList<Fleet> fleetsOfAllPlayers;

    public ArrayList<Planet> planets;
    public ArrayList<Fleet> fleets;

    public void resetData() {
        planets = new ArrayList<>();
        fleets = new ArrayList<>();
        planetsOfAllPlayers = new ArrayList<>();
        fleetsOfAllPlayers = new ArrayList<>();
    }

    public PlayerData(boolean neutral) {
        if (neutral) color = possibleColors[possibleColors.length - 1];
    }

    public void addNewPlanet(String[] tokens){
        Planet planet = new Planet(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], this);
        planets.add(planet);
        planetsOfAllPlayers.add(planet);
    }

    public void addNewFleet(String[] tokens){
        Fleet fleet = new Fleet(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], this);
        fleets.add(fleet);
        fleetsOfAllPlayers.add(fleet);
    }


    public static Planet findPlanetByName(int name){

        for (Planet obj : planetsOfAllPlayers) {
            if (obj.name == name) {
                return obj;
            }
        }
        return null;

    }



    public static void sortFleetsOfAllPlayers(){
        fleetsOfAllPlayers.sort(Comparator.comparingDouble(o -> o.getNeededTurns()));
    }

}
