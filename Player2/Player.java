import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


class AttackOrder{

	public Planet planet;
	public int score;
	public boolean canBeAttackByOthers;

	public AttackOrder(Planet planet, int score, boolean canBeAttackByOthers) {
		this.planet = planet;
		this.score = score;
		this.canBeAttackByOthers = canBeAttackByOthers;
	}

	public float getScore() {
		return score;
	}
}
public class Player {

	public static int turn = 0;

	public static int universeWidth;
	public static int universeHeight;



	public static PlayerData player;
	public static PlayerData teammate;

	public static PlayerData firstEnemy;
	public static PlayerData secondEnemy;

	public static PlayerData neutral;


	public static void main(String[] args) throws Exception {

		try {

			player = new PlayerData(false);
			teammate = new PlayerData(false);
			firstEnemy = new PlayerData(false);
			secondEnemy = new PlayerData(false);
			neutral = new PlayerData(true);

			while (true) {

				//Get game inputs
				getGameState();

				long startTime = System.currentTimeMillis();

				//We start after second turn because we don't know who is who before that
				if(turn > 1){


					for (int i = 0; i < player.planets.size(); i++) {


						Planet originPlanet = player.planets.get(i);

						ArrayList<AttackOrder> attackOrder = new ArrayList<>();


						for (int j = 0; j < Planet.planetsOfAllPlayers.size(); j++) {

							Planet destinationPlanet = Planet.planetsOfAllPlayers.get(j);

							if (destinationPlanet.player == player || destinationPlanet.player == teammate)continue;

							GameEmulation ge = new GameEmulation(Planet.planetsOfAllPlayers, Fleet.fleetsOfAllPlayers, 500);
							ge.runEmulation(originPlanet, destinationPlanet, (originPlanet.fleetSize / 3) * 2);


							boolean canBeAttackByOthers =
									originPlanet.isEnemyAbleToAttackBeforeMe(destinationPlanet, firstEnemy.planets) ||
									originPlanet.isEnemyAbleToAttackBeforeMe(destinationPlanet, secondEnemy.planets);


							attackOrder.add(0, new AttackOrder(destinationPlanet, ge.getScore(), canBeAttackByOthers));

						}



						//Run emulation without fleets
						GameEmulation ge = new GameEmulation(Planet.planetsOfAllPlayers, Fleet.fleetsOfAllPlayers, 500);
						ge.runEmulation();
						AttackOrder withoutAttack = new AttackOrder(null, ge.getScore(), false);

						//Go true data and decide what to attack
						attackOrder.sort(Comparator.comparingDouble(AttackOrder::getScore));


						if (withoutAttack.score < attackOrder.get(attackOrder.size() - 1).score){

							for (int j = attackOrder.size() - 1; j >= 0; j--) {

								AttackOrder attack = attackOrder.get(j);

								if (!attack.canBeAttackByOthers){
									attack(originPlanet.name, attack.planet.name, (originPlanet.fleetSize / 3) * 2);
									break;
								}

								if (j == 0){
									attack = attackOrder.get(attackOrder.size() - 1);
									attack(originPlanet.name, attack.planet.name, (originPlanet.fleetSize / 3) * 2);

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
				if (turn == 0)System.out.println("M NAME " + player.color);

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

	static void attack(int from, int to, int size){
		System.out.println("A " + from + " " + to + " " + size);
	}

	public static void getGameState() throws IOException {

		//Reset data from previous turn
		player.resetData();
		teammate.resetData();
		firstEnemy.resetData();
		secondEnemy.resetData();
		neutral.resetData();

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
					player.color = tokens[3];
					break;

				//Setup planets
				case 'P':
					getPlayerByColor(tokens[6]).addNewPlanet(tokens);
					break;

				//Setup fleet
				case 'F':
					getPlayerByColor(tokens[7]).addNewFleet(tokens);
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




	static PlayerData getPlayerByColor(String color){

		if (Objects.equals(player.color, color))return player;
		else if (Objects.equals(teammate.color, color))return teammate;
		else if (Objects.equals(firstEnemy.color, color))return firstEnemy;
		else if (Objects.equals(secondEnemy.color, color))return secondEnemy;
		else return neutral;

	}


	//This functions setup teammate and also finds who is enemy
	static void setTeammateAndEnemies(String teammateColor){

		teammate.color = teammateColor;

		//Find enemies
		for (String color : PlayerData.possibleColors) {

			if (
				Objects.equals(color, teammate.color)   ||
				Objects.equals(color, player.color)     ||
				Objects.equals(color, neutral.color)    ||
				Objects.equals(color, firstEnemy.color)
			)continue;

			if (Objects.equals(firstEnemy.color, "")) firstEnemy.color = color;
			else if (Objects.equals(secondEnemy.color, "")) secondEnemy.color = color;
			else break;

		}

	}

}



