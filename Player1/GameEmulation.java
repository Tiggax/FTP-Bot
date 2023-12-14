import java.util.ArrayList;
import java.util.Comparator;


public class GameEmulation {


    public ArrayList<Planet> planets;
    public ArrayList<Fleet> fleets;

    int turns;


    int playerScore = 0;
    int enemiesScore = 0;


    public GameEmulation(ArrayList<Planet> planets, ArrayList<Fleet> fleets, int turns) {

        this.planets = planets;
        this.fleets = fleets;
        this.turns = turns;

    }



    int runEmulation(Planet originPlanet, Planet destinationPlanet, int size) throws CloneNotSupportedException {

        if (originPlanet == null || destinationPlanet == null)return Integer.MIN_VALUE;

        //Subtract size so that it will not be included in final score
        playerScore = -size;

        //Calculate attack fleet stuff
        int neededTurns = (int)(Math.sqrt((originPlanet.positionX - destinationPlanet.positionX) *
                               (originPlanet.positionX - destinationPlanet.positionX) +
                               (originPlanet.positionY - destinationPlanet.positionY) *
                               (originPlanet.positionY - destinationPlanet.positionY))) / 2;
        Fleet attackFleet = new Fleet(Integer.MAX_VALUE, size, originPlanet, destinationPlanet, 0, neededTurns, originPlanet.player);

        //Add attacker (it will be removed at the end)
        fleets.add(attackFleet);

        //Run emulation
        int ret = runEmulation();

        //Remove attacker
        fleets.remove(attackFleet);


        return ret;
    }



    int runEmulation() throws CloneNotSupportedException {

        //Sort fleets so that we can emulate them in correct order
        fleets.sort(Comparator.comparingDouble(Fleet::getNeededTurns));

        //Emulate planet score by turns
        for (Planet planet : planets){

            Planet clonedPlanet = (Planet) planet.clone();
            emulatePlanetTurn(clonedPlanet);

            if (clonedPlanet.player == Player.player) playerScore += clonedPlanet.fleetSize;
            else enemiesScore += clonedPlanet.fleetSize;

        }

        //Add fleets that still exists after x turns to score
        for (Fleet fleet : fleets) {
            if (fleet.getNeededTurns() <= turns) continue;
            if (fleet.player == Player.player) playerScore += fleet.size;
            else enemiesScore += fleet.size;
        }

        //return myPlanets;
        return playerScore - enemiesScore;

    }



    private void emulatePlanetTurn(Planet planet){

        int previousTurn = 0;

        for (Fleet fleet : fleets) {

            if (fleet.getNeededTurns() > turns) break;
            if (fleet.destinationPlanet.name != planet.name) continue;

            int currentTurn = fleet.getNeededTurns();

            increasePlanetsFleets(planet, currentTurn - previousTurn);
            landFleetsToPlanet(fleet, planet);

            previousTurn = currentTurn;
        }

        increasePlanetsFleets(planet, turns - previousTurn);

    }


    private void increasePlanetsFleets(Planet planet, int byTurns){
        if (planet.player == Player.neutral)return;
        planet.fleetSize += planet.size * 10 * byTurns;
    }


    private void landFleetsToPlanet(Fleet fleet, Planet planet){

        if (fleet.player == planet.player) planet.fleetSize += fleet.size;
        else {

            planet.fleetSize -= fleet.size;

            if (planet.fleetSize < 0){
                planet.fleetSize *= -1;
                planet.player = fleet.player;
            }
        }
    }
}
