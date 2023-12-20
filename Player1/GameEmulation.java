import java.util.ArrayList;
import java.util.Comparator;


public class GameEmulation {

    private final ArrayList<Fleet> allFleets;

    private final Planet originPlanet;
    private final Planet destinationPlanet;


    private Fleet attackFleet;


    public GameEmulation(ArrayList<Fleet> allFleets, Planet originPlanet, Planet destinationPlanet) {
        this.allFleets = allFleets;
        this.originPlanet = originPlanet;
        this.destinationPlanet = destinationPlanet;
    }


    public float runEmulation(int turns, boolean attack) throws CloneNotSupportedException {
        //Sort fleets so that we can emulate them in correct order
        allFleets.sort(Comparator.comparingDouble(Fleet::getNeededTurns));
        return emulate(turns, attack);
    }

    Fleet getAttackFleet(){
        return attackFleet;
    }


    public float emulate(int turns, boolean attack) throws CloneNotSupportedException {

        int pointerToFleet = 0;

        for (int i = 0; i < turns; i++) {

            if(attack && originPlanet.player == Players.PLAYER) {

                Planet copyOfDestinationPlanet = (Planet) destinationPlanet.clone();
                Planet copyOfOriginPlanet = (Planet) originPlanet.clone();

                attackFleet = new Fleet(
                        Integer.MAX_VALUE,
                        (int)(copyOfOriginPlanet.fleetSize * 1.0) - 1,
                        copyOfOriginPlanet.name,
                        copyOfDestinationPlanet.name,
                        -i,
                        copyOfOriginPlanet.turnDistance(copyOfDestinationPlanet),
                        copyOfOriginPlanet.player);

                copyOfOriginPlanet.fleetSize -= attackFleet.size;
                allFleets.add(attackFleet);

                //Sort fleets so that we can emulate them in correct order
                allFleets.sort(Comparator.comparingDouble(Fleet::getNeededTurns));

                int secondPointerToFleet = pointerToFleet;

                for (int j = i; j < turns; j++) {

                    copyOfDestinationPlanet.fleetSize = getPlanetsFleets(copyOfDestinationPlanet, 1);
                    copyOfOriginPlanet.fleetSize = getPlanetsFleets(copyOfOriginPlanet, 1);

                    if (allFleets.size() > secondPointerToFleet){

                        Fleet secondFleet = allFleets.get(secondPointerToFleet);

                        while (secondFleet.getNeededTurns() == j) {

                            if (secondFleet.destinationPlanet == copyOfDestinationPlanet.name){
                                landFleetsToPlanet(secondFleet, copyOfDestinationPlanet);
                            }
                            if (secondFleet.destinationPlanet == copyOfOriginPlanet.name){
                                landFleetsToPlanet(secondFleet, copyOfOriginPlanet);
                            }
                            secondPointerToFleet++;

                            if (allFleets.size() <= secondPointerToFleet)break;
                            secondFleet = allFleets.get(secondPointerToFleet);
                        }
                    }

                }


                allFleets.remove(attackFleet);

                if (PlayerData.isInMyTeam(copyOfOriginPlanet.player) && PlayerData.isInMyTeam(copyOfDestinationPlanet.player)){
                    return (copyOfOriginPlanet.size + copyOfDestinationPlanet.size);
                }

            }



            destinationPlanet.fleetSize = getPlanetsFleets(destinationPlanet, 1);
            originPlanet.fleetSize = getPlanetsFleets(originPlanet, 1);

            if (allFleets.size() > pointerToFleet) {
                Fleet fleet = allFleets.get(pointerToFleet);

                while (fleet.getNeededTurns() == i) {

                    if (fleet.destinationPlanet == destinationPlanet.name){
                        landFleetsToPlanet(fleet, destinationPlanet);
                    }
                    if (fleet.destinationPlanet == originPlanet.name){
                        landFleetsToPlanet(fleet, originPlanet);
                    }
                    pointerToFleet++;
                    if (allFleets.size() <= pointerToFleet)break;
                    fleet = allFleets.get(pointerToFleet);
                }
            }


        }

        float ret = 0;

        if (PlayerData.isInMyTeam(originPlanet.player)){
            ret += originPlanet.size;
        }

        if (PlayerData.isInMyTeam(destinationPlanet.player)){
            ret += destinationPlanet.size;
        }

        return ret;

    }


    private void emulateAttackedPlanet(Planet planet, int turns){
        int previousTurn = 0;
        for (Fleet fleet : allFleets) {

            if (fleet.getNeededTurns() > turns) break;
            if (fleet.destinationPlanet != planet.name) continue;

            int currentTurn = fleet.getNeededTurns();
            if (currentTurn <= previousTurn)continue;

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
