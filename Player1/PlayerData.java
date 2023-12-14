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
        Planet planet = new Planet(
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]),
                Integer.parseInt(tokens[3]),
                Float.parseFloat(tokens[4]),
                Integer.parseInt(tokens[5]),
                this);

        planets.add(planet);
        planetsOfAllPlayers.add(planet);
    }

    public void addNewFleet(String[] tokens){

        Fleet fleet = new Fleet(
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]),
                findPlanetByName(Integer.parseInt(tokens[3])),
                findPlanetByName(Integer.parseInt(tokens[4])),
                Integer.parseInt(tokens[5]),
                Integer.parseInt(tokens[6]),
                this);

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


}
