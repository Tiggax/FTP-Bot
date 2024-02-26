import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


class AttackOrder{

	public Planet planet;
	public int score;
	public int canBeAttackByOthers;

	public Fleet fleet;



	public AttackOrder(Planet planet, int score, int canBeAttackByOthers, Fleet fleet) {
		this.planet = planet;
		this.score = score;
		this.canBeAttackByOthers = canBeAttackByOthers;
		this.fleet = fleet;
	}

	public float getScore() {
		return score;
	}

	public int getDistance(){
		return fleet.neededTurns;
	}

	public int getCanBeAttackByOthers() {
		return canBeAttackByOthers;
	}
}
public class Player {

	public static int turn = 0;

	public static int universeWidth;
	public static int universeHeight;


	private static final float maxAttackRatio = 1.0f;
	private static final int emulateTurns = 50;
	private static final int emulateAttackTime = 100;



	public static void main(String[] args) throws Exception {

		try {

			while (true) {

				//Get game inputs
				getGameState();

				long startTime = System.currentTimeMillis();

				//We start after second turn because we don't know who is who before that
				if (turn > 1) {

					for (int i = 0; i < Planet.planets.size(); i++) {

						Planet originPlanet = Planet.planets.get(i);

						if (originPlanet.player != Players.PLAYER) continue;
						//if (originPlanet.player != Players.PLAYER && originPlanet.player != Players.TEAMMATE) continue;

						ArrayList<AttackOrder> attackOrder = new ArrayList<>();

						for (int j = 0; j < Planet.planets.size(); j++) {

							Planet destinationPlanet = Planet.planets.get(j);

							//Prevent attacking itself
							if (originPlanet == destinationPlanet) continue;


							//Check if attacking planet can be reinforced
							int canBeAttackByOthers = 0;

							for (Planet planet : Planet.planets) {

								if (PlayerData.isInMyTeam(planet.player)) continue;
								if (planet.player == destinationPlanet.player) continue;

								if (planet.turnDistance(destinationPlanet) < originPlanet.turnDistance(destinationPlanet))++canBeAttackByOthers;

							}

							GameEmulation ge_0 = new GameEmulation(originPlanet, destinationPlanet, null, emulateTurns);
							int scoreWithoutAttack = ge_0.runEmulation();


							for (int k = 0; k < emulateAttackTime; k++) {

								Fleet attackFleet = new Fleet(
										Integer.MAX_VALUE,
										(int)(originPlanet.getFleetSize(k) * maxAttackRatio) - 1,
										originPlanet.name,
										destinationPlanet.name,
										-k,
										originPlanet.turnDistance(destinationPlanet),
										originPlanet.player);

								GameEmulation ge_1 = new GameEmulation(originPlanet, destinationPlanet, attackFleet, emulateTurns);
								int score = ge_1.runEmulation() - scoreWithoutAttack;

								if(score > 0) {
									attackOrder.add(new AttackOrder(originPlanet, score, canBeAttackByOthers, attackFleet));
									break;
								}

							}

						}

						if (attackOrder.isEmpty()) continue;

						//Go true data and decide what to attack
						attackOrder.sort(Comparator.comparingDouble(AttackOrder::getDistance));

						for (int j = 0; j < attackOrder.size(); j++) {

							AttackOrder attack = attackOrder.get(j);

							if (attack.canBeAttackByOthers == 0) {
								attack(attack.fleet, originPlanet);
								break;
							}

							if (j == (attackOrder.size() - 1)) {
								attack = attackOrder.get(0);
								attack(attack.fleet, originPlanet);
								break;
							}

						}

					}

				}


				// Record the end time
				long endTime = System.currentTimeMillis();

				// Calculate and print the elapsed time
				long elapsedTime = endTime - startTime;
				Log.print("Elapsed Time: " + elapsedTime + " milliseconds");


				//First turn we meet our teammate
				if (turn == 0)System.out.println("M NAME " + PlayerData.getPlayerColor(Players.PLAYER));

				//Sending done
				System.out.println("E");

				//Track turns
				turn++;


			}

		} catch (Exception e) {

			Log.print("ERROR: ");
			Log.print(e.getMessage());

			e.printStackTrace();

		}

		//Before ending class
		Log.closeFile();

	}

	static void attack(Fleet fleet, Planet originPlanet) throws IOException {

		//Check if attack can be done
		if (0 > fleet.currentTurn) return;
		if (originPlanet.fleetSize * maxAttackRatio < fleet.size) return;

		originPlanet.fleetSize -= fleet.size;
		Planet.addFleet(fleet);
		if (fleet.player != Players.PLAYER) return;
		System.out.println("A " + fleet.originPlanet + " " + fleet.destinationPlanet + " " + fleet.size);
	}

	public static void getGameState() throws IOException {

		//Reset data from previous turn
		Planet.planets = new ArrayList<>();

		BufferedReader stdin = new BufferedReader(new java.io.InputStreamReader(System.in));

		while (true) {

			String line = stdin.readLine();
			String[] tokens = line.split(" ");


			switch (line.charAt(0)){

				//Return if it is last line
				case 'S':
					return;

				//Setup universe
				case 'U':
					universeWidth = Integer.parseInt(tokens[1]);
					universeHeight = Integer.parseInt(tokens[2]);
					PlayerData.setColor(Players.PLAYER, tokens[3]);
					break;

				//Setup planets
				case 'P':
					Planet.addNewPlanet(tokens);
					break;

				//Setup fleet
				case 'F':
					Planet.addNewFleet(tokens);
					break;

				//Someone died (string is not in color????????? this data is totally useless)
				case 'L':
					break;

				//Get teammate data
				case 'M':
					if (Objects.equals(tokens[1], "NAME")) setTeammateAndEnemies(tokens[2]);
					break;

				default:
					break;
			}
		}
	}





	//This functions setup teammate and also finds who are enemies
	static void setTeammateAndEnemies(String teammateColor) {

		PlayerData.setColor(Players.TEAMMATE, teammateColor);

		//Find enemies
		for (String color : PlayerData.possibleColors) {

			if (
				Objects.equals(color, PlayerData.getPlayerColor(Players.TEAMMATE))   ||
				Objects.equals(color, PlayerData.getPlayerColor(Players.PLAYER))     ||
				Objects.equals(color, PlayerData.getPlayerColor(Players.NEUTRAL))    ||
				Objects.equals(color, PlayerData.getPlayerColor(Players.FIRST_ENEMY))
			)continue;

			if (PlayerData.getPlayerColor(Players.FIRST_ENEMY) == null){
				PlayerData.setColor(Players.FIRST_ENEMY, color);
			}
			else if (PlayerData.getPlayerColor(Players.SECOND_ENEMY) == null){
				PlayerData.setColor(Players.SECOND_ENEMY, color);
			}
			else break;

		}

	}

}



