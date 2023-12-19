import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;


public class GameEmulation {

    private final ArrayList<Fleet> allFleets;

    private final Planet originPlanet;
    private final Planet destinationPlanet;
    private final Fleet attackFleet;

    private final int turns;


    private int teamScore = 0;
    private int enemiesScore = 0;


    public GameEmulation(ArrayList<Fleet> allFleets, Planet originPlanet, Planet destinationPlanet, Fleet attackFleet, int turns) {
        this.allFleets = allFleets;
        this.originPlanet = originPlanet;
        this.destinationPlanet = destinationPlanet;
        this.attackFleet = attackFleet;
        this.turns = turns;
    }


    public int runEmulation() throws IOException {

        if(attackFleet != null){
            allFleets.add(attackFleet);
            //teamScore = -attackFleet.size;
        }

        //Sort fleets so that we can emulate them in correct order
        allFleets.sort(Comparator.comparingDouble(Fleet::getNeededTurns));

        emulateOriginalPlanet(originPlanet);
        if (PlayerData.isInMyTeam(originPlanet.player))teamScore += originPlanet.fleetSize;
        else enemiesScore += originPlanet.fleetSize;

        emulateAttackedPlanet(destinationPlanet);
        if (PlayerData.isInMyTeam(destinationPlanet.player))teamScore += destinationPlanet.fleetSize;
        else enemiesScore += destinationPlanet.fleetSize;


        if(attackFleet != null)allFleets.remove(attackFleet);

        return teamScore - enemiesScore;
    }




    private void emulateOriginalPlanet(Planet planet) throws IOException {

        int previousTurn = Integer.MIN_VALUE;

        for (Fleet fleet : allFleets) {

            if (fleet.getNeededTurns() > turns) break;

            int currentTurn = fleet.getNeededTurns();




            if (attackFleet != null && previousTurn != currentTurn){

                if ((-attackFleet.currentTurn) <= currentTurn && (-attackFleet.currentTurn) > previousTurn){

                    int newPlanetFleetSize = getPlanetsFleets(planet, (-attackFleet.currentTurn) - previousTurn);

                    if (newPlanetFleetSize <= attackFleet.size || planet.player != attackFleet.player){
                        attackFleet.size = 0;
                    }
                    else {

                        planet.fleetSize = newPlanetFleetSize;
                        planet.fleetSize -= attackFleet.size;
                        previousTurn = (-attackFleet.currentTurn);

                    }

                }

            }



            if (fleet.destinationPlanet == planet.name) {

                planet.fleetSize = getPlanetsFleets(planet, currentTurn - previousTurn);
                landFleetsToPlanet(fleet, planet);
                previousTurn = currentTurn;

            }

        }

        planet.fleetSize = getPlanetsFleets(planet, turns - previousTurn);

    }




    private void emulateAttackedPlanet(Planet planet){

        int previousTurn = Integer.MIN_VALUE;

        for (Fleet fleet : allFleets) {

            if (fleet.getNeededTurns() > turns) break;
            if (fleet.destinationPlanet != planet.name) continue;

            int currentTurn = fleet.getNeededTurns();

            planet.fleetSize = getPlanetsFleets(planet, currentTurn - previousTurn);
            landFleetsToPlanet(fleet, planet);
            previousTurn = currentTurn;

        }

        planet.fleetSize = getPlanetsFleets(planet, turns - previousTurn);

    }


    private int getPlanetsFleets(Planet planet, int turns){
        if (planet.player == Players.NEUTRAL)return planet.fleetSize;
        return planet.getFleetSize(turns);
    }


    private void landFleetsToPlanet(Fleet fleet, Planet planet){

        if (fleet.size <= 0)return;

        planet.fleetSize += fleet.size * addOrSub(fleet.player, planet.player);

        if (planet.fleetSize < 0){
            planet.fleetSize *= -1;
            planet.player = fleet.player;
        }

    }

    private int addOrSub(Players first, Players second){

        if(first == second)return 1;

        if (first == Players.FIRST_ENEMY && second == Players.SECOND_ENEMY)return 1;
        if (first == Players.SECOND_ENEMY && second == Players.FIRST_ENEMY)return 1;

        if (first == Players.PLAYER && second == Players.TEAMMATE)return 1;
        if (first == Players.TEAMMATE && second == Players.PLAYER)return 1;

        return -1;
    }

}
