import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Planet implements Cloneable {


    public static ArrayList<Planet> planets = new ArrayList<>();

    public static final int speed = 2;
    public static final int fleetSizeIncreaseByTurn = 10;


    public final int name;

    public final int positionX;
    public final int positionY;

    public final float size;
    public int fleetSize;

    private final ArrayList<Fleet> attackingFleets = new ArrayList<>();

    public Players player;

    private static final int[] playersPlanetFleetCount = new int[Players.values().length];
    private static final float[] playersPlanetFleetSize = new float[Players.values().length];

    public Planet(int name, int positionX, int positionY, float size, int fleetSize, Players player) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;
        this.fleetSize = fleetSize;
        this.player = player;
    }

    public boolean amIAttacked() {
        for (Fleet attackingFleet : attackingFleets) {
            if (attackingFleet.player == Players.FIRST_ENEMY) return true;
            if (attackingFleet.player == Players.SECOND_ENEMY) return true;
        }
        return false;
    }

    public static int getPlayerPlanetCount(Players player){
        return playersPlanetFleetCount[player.ordinal()];
    }

    public static float getPlayerPlanetSize(Players player){
        return playersPlanetFleetSize[player.ordinal()];
    }

    public int getAttackingFleetsSize(){
        return attackingFleets.size();
    }

    public Fleet getAttackingFleets(int index){
        return attackingFleets.get(index);
    }


    //Add attacking fleet in order
    public void addAttackingFleets(Fleet fleet){
        int index = Collections.binarySearch(attackingFleets, fleet, Comparator.comparingDouble(Fleet::getNeededTurns));
        index = (index < 0) ? -index - 1 : index;
        attackingFleets.add(index, fleet);
    }

    public void removeAttackingFleet(Fleet fleet){
        attackingFleets.remove(fleet);
    }

    public static void addNewFleet(String[] tokens) throws IOException {

        addFleet(
                new Fleet(
                        Integer.parseInt(tokens[1]),
                        Integer.parseInt(tokens[2]),
                        Integer.parseInt(tokens[3]),
                        Integer.parseInt(tokens[4]),
                        Integer.parseInt(tokens[5]),
                        Integer.parseInt(tokens[6]),
                        PlayerData.getPlayerByColor(tokens[7]))
        );

    }

    public static void addFleet(Fleet fleet) throws IOException {

        for (Planet planet : planets) if (planet.name == fleet.destinationPlanet) {
            planet.addAttackingFleets(fleet);
            return;
        }
        Log.print("Fleet cannot be added to destination planet!");

    }

    public static void addNewPlanet(String[] tokens){
        Planet planet = new Planet(
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]),
                Integer.parseInt(tokens[3]),
                Float.parseFloat(tokens[4]),
                Integer.parseInt(tokens[5]),
                PlayerData.getPlayerByColor(tokens[6]));

        ++playersPlanetFleetCount[planet.player.ordinal()];
        playersPlanetFleetSize[planet.player.ordinal()] += planet.size;

        Planet.planets.add(planet);
    }

    public int turnDistance(Planet planet){
        return (int)(Math.sqrt((positionX - planet.positionX) *
                (positionX - planet.positionX) +
                (positionY - planet.positionY) *
                        (positionY - planet.positionY)
        )) / speed;
    }

    public int getFleetSize(int turns){
        return (int)(turns * fleetSizeIncreaseByTurn * size + fleetSize);
    }

    public int getDistanceToClosestEnemy() {

        int distance = Integer.MAX_VALUE;

        for (Planet planet : planets) {
            if (planet.player != Players.FIRST_ENEMY && planet.player != Players.SECOND_ENEMY) {
                if (!planet.amIAttacked()) continue;
            }
            int newDistance = turnDistance(planet);
            if (newDistance >= distance) continue;
            distance = newDistance;
        }

        return distance;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
