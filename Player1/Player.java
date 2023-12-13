import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

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

				//Test for attack
				for (int i = 0; i < player.planets.size(); i++) {
					//if (!teammate.planets.isEmpty())attack(player.planets.get(i).name, teammate.planets.get(0).name, 1);
					if(turn == 2 && !firstEnemy.planets.isEmpty())attack(player.planets.get(i).name, firstEnemy.planets.get(0).name, 100);



					//else if (!secondEnemy.planets.isEmpty())attack(player.planets.get(i).name, secondEnemy.planets.get(0).name, 100);
				}

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



