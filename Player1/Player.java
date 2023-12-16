import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


class AttackOrder{

	public Planet planet;
	public int score;
	public boolean canBeAttackByOthers;

	public Fleet fleet;


	public AttackOrder(Planet planet, int score, boolean canBeAttackByOthers, Fleet fleet) {
		this.planet = planet;
		this.score = score;
		this.canBeAttackByOthers = canBeAttackByOthers;
		this.fleet = fleet;
	}

	public float getScore() {
		return score;
	}
}
public class Player {


	public static int synchronize = 0;
	public static int turn = 0;

	public static int universeWidth;
	public static int universeHeight;


	private static final float maxAttackRatio = 2.0f / 3.0f;



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

						ArrayList<AttackOrder> attackOrder = new ArrayList<>();



						for (int j = 0; j < Planet.planets.size(); j++) {

							Planet destinationPlanet = Planet.planets.get(j);

							//Check if attacking planet can be reinforced
							boolean canBeAttackByOthers = false;

							for (Planet planet : Planet.planets) {

								if (PlayerData.isInMyTeam(planet.player)) continue;
								if (planet.player == destinationPlanet.player) continue;

								if (planet.turnDistance(destinationPlanet) < originPlanet.turnDistance(destinationPlanet)) {
									canBeAttackByOthers = true;
									break;
								}
							}


							//Get best attack for x rounds from now
							AttackOrder bestAttackResult = null;

							for (int k = 0; k < 10; k++) {

								Fleet attackFleet = new Fleet(
										Integer.MAX_VALUE,
										(int)((k * 10 * originPlanet.size + originPlanet.fleetSize) * maxAttackRatio),
										originPlanet.name,
										destinationPlanet.name,
										-k,
										originPlanet.turnDistance(destinationPlanet),
										originPlanet.player);

								GameEmulation ge = new GameEmulation(Planet.planets, Fleet.fleets, 1000);
								ge.runEmulation(attackFleet);

								AttackOrder attackResult = new AttackOrder(destinationPlanet, ge.getScore(), canBeAttackByOthers, attackFleet);

								if (bestAttackResult == null){
									bestAttackResult = attackResult;
									continue;
								}

								if (attackResult.score > bestAttackResult.score){
									bestAttackResult = attackResult;
									break;
								}

							}

							attackOrder.add(0, bestAttackResult);

						}


						//Return if there is no planet to attack
						if (attackOrder.isEmpty())continue;


						//Run emulation without fleets (we need this to compare it with attacks)
						GameEmulation ge = new GameEmulation(Planet.planets, Fleet.fleets, 1000);
						ge.runEmulation();
						AttackOrder withoutAttack = new AttackOrder(null, ge.getScore(), false, null);

						//Go true data and decide what to attack
						attackOrder.sort(Comparator.comparingDouble(AttackOrder::getScore));

						if (withoutAttack.score < attackOrder.get(attackOrder.size() - 1).score) {

							for (int j = attackOrder.size() - 1; j >= 0; j--) {

								AttackOrder attack = attackOrder.get(j);


								if (withoutAttack.score < attack.score) {

									if (!attack.canBeAttackByOthers) {
										attack(attack.fleet, originPlanet);
										break;
									}

								}

								if (j == 0) {

									attack = attackOrder.get(attackOrder.size() - 1);
									attack(attack.fleet, originPlanet);
									break;

								}

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


			for (int i = 0; i < e.getStackTrace().length; i++) {
				//Log.print(e.getStackTrace()[0].getLineNumber() + "");
				//Log.print(e.getStackTrace()[0].getClassName() + "");
				//Log.print(e.getStackTrace()[0].getClassLoaderName() + "");
				//Log.print(e.getStackTrace()[0].getFileName() + "");
				//Log.print(e.getStackTrace()[0].getMethodName() + "");
				//Log.print(e.getStackTrace()[0].getModuleName() + "");
				//Log.print(e.getStackTrace()[0].getModuleVersion() + "");
			}


			e.printStackTrace();

		}

		//Before ending class
		Log.closeFile();

	}

	static void attack(Fleet fleet, Planet originPlanet){

		//Check if attack can be done
		if (0 > fleet.currentTurn)return;
		if (originPlanet.fleetSize < fleet.size)return;

		originPlanet.fleetSize -= fleet.size;
		Fleet.fleets.add(fleet);
		System.out.println("A " + fleet.originPlanet + " " + fleet.destinationPlanet + " " + fleet.size);
	}

	public static void getGameState() throws IOException {

		//Reset data from previous turn
		Planet.planets = new ArrayList<>();
		Fleet.fleets = new ArrayList<>();

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
					Fleet.addNewFleet(tokens);
					break;

				//Someone died (string is not in color????????? this data is totally useless)
				case 'L':
					break;

				//Get teammate data
				case 'M':
					if (Objects.equals(tokens[1], "NAME"))setTeammateAndEnemies(tokens[2]);
					break;

				default:
					break;
			}
		}
	}





	//This functions setup teammate and also finds who are enemies
	static void setTeammateAndEnemies(String teammateColor){

		PlayerData.setColor(Players.TEAMMATE, teammateColor);

		//Set synchronization
		for (String color : PlayerData.possibleColors) {
			if (Objects.equals(color, PlayerData.getPlayerColor(Players.PLAYER))){
				synchronize = 0;
				break;
			}
			if (Objects.equals(color, PlayerData.getPlayerColor(Players.TEAMMATE))){
				synchronize = 1;
				break;
			}
		}

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



