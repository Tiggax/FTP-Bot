import java.util.ArrayList;

public class Planet implements Cloneable {


    public static ArrayList<Planet> planetsOfAllPlayers;

    public static final int speed = 2;

    public int name;

    public int positionX;
    public int positionY;

    public float size;
    public int fleetSize;

    public PlayerData player;

    public Planet(int name, int positionX, int positionY, float size, int fleetSize, PlayerData player) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;
        this.fleetSize = fleetSize;
        this.player = player;
    }


    public static Planet findPlanetByName(int name){

        for (Planet obj : Planet.planetsOfAllPlayers) {
            if (obj.name == name) {
                return obj;
            }
        }
        return null;

    }


    public int turnDistance(Planet planet){
        return (int)(Math.sqrt((positionX - planet.positionX) *
                               (positionX - planet.positionX) +
                               (positionY - planet.positionY) *
                               (positionY - planet.positionY)
        )) / speed;
    }

    public boolean isEnemyAbleToAttackBeforeMe(Planet planetToAttack, ArrayList<Planet> enemyPlanets){

        for (Planet planet : enemyPlanets) {
            if (planetToAttack == planet)continue;
            if (planet.turnDistance(planetToAttack) < turnDistance(planetToAttack)) return true;
        }

        return false;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
