package Game2022.Client;

import Game2022.Extra.Direction;
import Game2022.Extra.Pair;
import Game2022.Extra.Player;

import java.util.ArrayList;
import java.util.List;


public class Controller {
    public static List<Player> players = new ArrayList<>();

    public static Player playerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) return player;
        }
        return null;
    }

    public static void startGame(String string) {
        String[] data = string.split(";", 2);

        // ----------------- unpack Map ----------------

        String[] mapStrings = data[0].split(":");
        int height = mapStrings[0].length();
        int width = mapStrings.length;
        char[][] map = new char[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = mapStrings[x].charAt(y);
            }
        }
        Gui.setMap(map);
        // ----------------- setup players ----------------
        String[] playerStrings = data[1].split(",");
        for (String playerString : playerStrings) {
            String[] userString = playerString.split(" ");
            if (userString.length <= 0) continue;
            Player temp = players.get(Integer.parseInt(userString[0]));
            temp.setXPos(Integer.parseInt(userString[1]));
            temp.setYPos(Integer.parseInt(userString[2]));
        }
        Gui.switchScene();
    }

    public static void playerJoin(String string) {
        String[] data = string.split(" ");
        Player player = new Player(Integer.parseInt(data[0]), data[1]);
        players.add(player);
        Gui.addPlayer(player);
        System.out.println("Client - " + player + " has joined the Lobby");
    }

    public static void movePlayer(String s) {
        String[] data = s.split(" ");
        Player player = playerById(Integer.parseInt(data[0]));
        if (player == null) return;
        Controller.updatePlayer(player, Integer.parseInt(data[1]), Integer.parseInt(data[2]), Enum.valueOf(Direction.class, data[3]));
    }

    public static void playerLeft(String s) {
        Player player = playerById(Integer.parseInt(s));
        System.out.println(player + " left.");
        Gui.removePlayer(player);
    }

    public static void playerKilled(String s) {
        Player player = playerById(Integer.parseInt(s));
        if (player == null) return;
        if (players.get(0) == player) {
            Gui.lost(player.getName());
        } else {
            System.out.println(player.getName() + " died!");
        }
    }

    public static void win(String s) {
        Player player = playerById(Integer.parseInt(s));
        if (player == null) return;
        if (players.get(0) == player) {
            Gui.win(player.getName());
        } else {
            System.out.println(player.getName() + " Won!");
        }
    }

    public static void pointsAdd(String s) {
        Player player = playerById(Integer.parseInt(s));
        if (player == null) return;
        player.addPoints(1);
        Gui.updateScoreTable();
    }

    public static void updateTile(String s) {
        String[] data = s.split(",");
        Pair pos = new Pair(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
        Gui.map[pos.getX()][pos.getY()] = data[2].charAt(0);
        Gui.updateField(pos);
    }

    public static void updatePlayer(Player player, int delta_x, int delta_y, Direction direction) {
        Pair oldPos = player.getLocation();
        Pair newPos = new Pair(delta_x, delta_y);

        Gui.movePlayerOnScreen(oldPos, newPos, direction);

        player.setDirection(direction);
        player.setLocation(newPos);
    }

    //----------------------------------------------------------------

//    public static void makePlayers(String name) {
//        Pair p = getRandomFreePosition();
//        //players.add(me);
//        p = getRandomFreePosition();
//    }
//
//    public static Pair getRandomFreePosition() {
//        // finds a random new position which is not wall
//        // and not occupied by other players
//        int x = 1;
//        int y = 1;
//        boolean foundfreepos = false;
//        while (!foundfreepos) {
//            Random r = new Random();
//            x = Math.abs(r.nextInt() % 18) + 1;
//            y = Math.abs(r.nextInt() % 18) + 1;
//            // er det gulv ?
//            if (Gui.map[x][y] == ' ') {
//                foundfreepos = true;
//                for (Player p : players) {//pladsen optaget af en anden
//                    if (p.getXPos() == x && p.getYPos() == y) foundfreepos = false;
//                }
//            }
//        }
//        Pair p = new Pair(x, y);
//        return p;
//    }

}
