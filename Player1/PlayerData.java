import java.util.ArrayList;

public class PlayerData {

    public static String[] possibleColors = {"blue", "cyan", "green", "yellow", "null"};

    public String color = "";

    public ArrayList<Planet> planets;
    public ArrayList<Fleet> fleets;

    public void resetData() {
        planets = new ArrayList<>();
        fleets = new ArrayList<>();
    }

    public PlayerData(boolean neutral) {
        if (neutral) color = possibleColors[possibleColors.length - 1];
    }

    public void addPlanet(Planet planet){
        planets.add(planet);
        Planet.allPlanets.add(planet);
    }

    public void addFleet(Fleet fleet){
        fleets.add(fleet);
        Fleet.allFleets.add(fleet);
    }

}
