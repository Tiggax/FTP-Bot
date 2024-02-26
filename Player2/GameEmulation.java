public class GameEmulation {

    private static final int startTurn = Integer.MIN_VALUE;

    private final Planet originPlanet;
    private final Planet destinationPlanet;
    private final Fleet attackFleet;

    private final int turns;



    public GameEmulation(Planet originPlanet, Planet destinationPlanet, Fleet attackFleet, int turns) {
        this.originPlanet = originPlanet;
        this.destinationPlanet = destinationPlanet;
        this.attackFleet = attackFleet;
        this.turns = turns;
    }


    public int runEmulation() throws CloneNotSupportedException {

        int score = 0;

        if(attackFleet != null) destinationPlanet.addAttackingFleets(attackFleet);

        for (int i = 0; i < Planet.planets.size(); i++) {

            Planet planet = (Planet) Planet.planets.get(i).clone();

            emulatePlanet(planet);
            if (PlayerData.isInMyTeam(planet.player)) score += planet.fleetSize;
            else score -= planet.fleetSize;

        }

        if(attackFleet != null) destinationPlanet.removeAttackingFleet(attackFleet);

        return score;
    }




    private void emulatePlanet(Planet planet) {

        int previousTurn = startTurn;

        for (int i = 0; i < planet.getAttackingFleetsSize(); i++) {

            Fleet fleet = planet.getAttackingFleets(i);

            if (fleet.getNeededTurns() > turns) break;
            int currentTurn = fleet.getNeededTurns();

            if(planet.name == originPlanet.name) previousTurn = tryAttackingDestination(planet, previousTurn, currentTurn);

            planet.fleetSize = getPlanetsFleets(planet, currentTurn - previousTurn);
            landFleetsToPlanet(fleet, planet);
            previousTurn = currentTurn;

        }

        if(planet.name == originPlanet.name) previousTurn = tryAttackingDestination(planet, previousTurn, turns);

        planet.fleetSize = getPlanetsFleets(planet, turns - previousTurn);

    }


    private int tryAttackingDestination(Planet planet, int previousTurn, int currentTurn){

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
        return previousTurn;
    }



    private int getPlanetsFleets(Planet planet, int turns){
        if (planet.player == Players.NEUTRAL) return planet.fleetSize;
        return planet.getFleetSize(turns);
    }


    private void landFleetsToPlanet(Fleet fleet, Planet planet){

        if (fleet.size <= 0) return;

        planet.fleetSize += fleet.size * addOrSub(fleet.player, planet.player);

        if (planet.fleetSize <= 0){
            planet.fleetSize *= -1;
            planet.player = fleet.player;
        }

    }

    private int addOrSub(Players first, Players second) {

        if (first == second) return 1;

        switch (first) {
            case FIRST_ENEMY:
                return (second == Players.SECOND_ENEMY) ? 1 : -1;
            case SECOND_ENEMY:
                return (second == Players.FIRST_ENEMY) ? 1 : -1;
            case PLAYER:
                return (second == Players.TEAMMATE) ? 1 : -1;
            case TEAMMATE:
                return (second == Players.PLAYER) ? 1 : -1;
            default:
                return -1;
        }

    }
}
