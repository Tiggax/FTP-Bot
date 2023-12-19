import java.util.Objects;


enum Players{

    PLAYER,
    TEAMMATE,
    FIRST_ENEMY,
    SECOND_ENEMY,
    NEUTRAL
}



public class PlayerData {


    public static final String[] possibleColors = {"blue", "cyan", "green", "yellow", "null"};


    private static final Players[] players = Players.values();
    private static final String[] playersColor = new String[players.length];


    public static void setColor(Players player, String color){
        playersColor[player.ordinal()] = color;
    }

    public static Players getPlayerByColor(String color){

        for (int i = 0; i < players.length; i++) {
            if (Objects.equals(playersColor[i], color))return players[i];
        }

        return players[Players.NEUTRAL.ordinal()];

    }


    static String getPlayerColor(Players player){
        return playersColor[player.ordinal()];
    }


    public static boolean isInMyTeam(Players player){
        return player == Players.PLAYER || player == Players.TEAMMATE;
    }











}
