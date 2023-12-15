import java.util.ArrayList;
import java.util.Comparator;


public class GameEmulation {


    public ArrayList<Planet> planets;
    public ArrayList<Fleet> fleets;

    int turns;


    int teamScore = 0;
    int enemiesScore = 0;



    public GameEmulation(ArrayList<Planet> planets, ArrayList<Fleet> fleets, int turns) {

        this.planets = planets;
        this.fleets = fleets;
        this.turns = turns;

    }


    public int getScore(){
        return teamScore - enemiesScore;
    }


    void runEmulation(Planet originPlanet, Planet destinationPlanet, int size) throws CloneNotSupportedException {

        //Subtract size so that it will not be included in final score
        teamScore = -size;

        //Calculate attack fleet stuff
        Fleet attackFleet = new Fleet(
                Integer.MAX_VALUE,
                size,
                originPlanet,
                destinationPlanet,
                0,
                originPlanet.turnDistance(destinationPlanet),
                originPlanet.player);

        //Add attacker (it will be removed at the end)
        fleets.add(attackFleet);

        //Run emulation
        runEmulation();

        //Remove attacker
        fleets.remove(attackFleet);

    }



    void runEmulation() throws CloneNotSupportedException {

        //Sort fleets so that we can emulate them in correct order
        fleets.sort(Comparator.comparingDouble(Fleet::getNeededTurns));

        //Emulate planet score by turns
        for (Planet planet : planets){

            Planet clonedPlanet = (Planet) planet.clone();
            emulatePlanetTurn(clonedPlanet);

            if (clonedPlanet.player.isInMyTeam())teamScore += clonedPlanet.fleetSize;
            else enemiesScore += clonedPlanet.fleetSize;

        }

        //Add fleets that still exists after x turns to score
        for (Fleet fleet : fleets) {
            if (fleet.getNeededTurns() <= turns) continue;
            if (fleet.player.isInMyTeam()) teamScore += fleet.size;
            else enemiesScore += fleet.size;
        }

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

        planet.fleetSize += fleet.size * addOrSub(fleet.player, planet.player);

        if (planet.fleetSize < 0){
            planet.fleetSize *= -1;
            planet.player = fleet.player;
        }

    }

    private int addOrSub(PlayerData first, PlayerData second){

        if(first == second)return 1;

        if (first == Player.firstEnemy && second == Player.secondEnemy)return 1;
        if (first == Player.secondEnemy && second == Player.firstEnemy)return 1;

        if (first == Player.player && second == Player.teammate)return 1;
        if (first == Player.teammate && second == Player.player)return 1;

        return -1;
    }

}
