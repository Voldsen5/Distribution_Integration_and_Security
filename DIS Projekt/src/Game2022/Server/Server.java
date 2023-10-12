package Game2022.Server;

import Game2022.Extra.Direction;
import Game2022.Extra.Player;
import Game2022.Extra.Pair;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    private static ServerSocket serverSocket;
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();
    //----------------------------------------------
    private static final int maxPlayers = 4;
    private static boolean allReady = false;
    public static char[][] map;
    public static final ArrayList<Bomb> bombs = new ArrayList<>();
    public static StringBuilder gameData = new StringBuilder();

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        new Thread(this::incoming).start();
        try {
            System.out.println("Server - Started at: " + InetAddress.getLocalHost().getHostAddress() + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void incoming() {
        while (!serverSocket.isClosed()) {
            try {
                new ClientHandler(serverSocket.accept());
            } catch (IOException ignore) {
            }
        }
    }

    public static void loadMap() {
        // Loads map1 from ServerStorage from a String[] to a char[][].
        // Because its easier to work with. :)
        int width = ServerStorage.map1[0].length();
        int height = ServerStorage.map1.length;
        map = new char[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = ServerStorage.map1[y].charAt(x);
            }
        }
    }

    private int getNextID() {
        temp:
        for (int i = 0; i < maxPlayers; i++) {
            for (ClientHandler connection : clients) {
                if (connection.player.getId() == i) {
                    continue temp;
                }
            }
            return i;
        }
        return -1;
    }

    public static Bomb getBomb(int x, int y) {
        for (Bomb bomb : bombs) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return bomb;
            }
        }
        return null;
    }


    private void placeBomb(int x, int y, int power) {
        if (getBomb(x, y) != null) {
            return;
        }
        updateTile(x, y, '0');
        Bomb bomb = new Bomb(x, y, power, 2);
        bombs.add(bomb);
        bomb.run();
    }

    public static void killAt(int x, int y) {
        for (ClientHandler client : clients) {
            if (client.player.getXPos() == x && client.player.getYPos() == y) {
                client.isAlive = false;
                System.out.println("Server - " + client.player + " died.");
                client.out.println("K," + client.player.getId());
                findWinner();
            }

        }
    }

    public static void findWinner() {
        ArrayList<ClientHandler> remaining = new ArrayList<>(clients);
        for (ClientHandler client1 : clients) {
            if (!client1.isAlive) {
                remaining.remove(client1);
            }
        }
        if (remaining.size() > 1) {
            return;
        }

        if (remaining.size() == 1) {
            for (ClientHandler client : clients) {
                client.out.println("W," + remaining.get(0).player.getId());
                remaining.get(0).player.addPoints(1);
                client.out.println("P," + remaining.get(0).player.getId());
            }
        }

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startGame();

            for (ClientHandler client : clients) {
                client.out.println(gameData);
            }
        }).start();

    }

    public static void startGame() {
        loadMap();
        for (ClientHandler client : clients) {
            // set player start position according to the players' ID.
            client.player.setLocation(switch (client.player.getId()) {
                case 0 -> new Pair(1, 1);
                case 1 -> new Pair(18, 1);
                case 2 -> new Pair(1, 18);
                case 3 -> new Pair(18, 18);
                default -> null;
            });
            client.isAlive = true;
        }
        // ----------------------- Initializing game ----------------------
        gameData = new StringBuilder("S,");
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                gameData.append(map[x][y]);
            }
            gameData.append(":");
        }
        gameData.append(";");

        for (ClientHandler client : clients) {
            gameData.append(client.player.getId()).append(" ").append(client.player.getLocation()).append(",");
        }
    }


    public static void updateTile(int x, int y, char object) {
        if (0 < x && x < map[0].length && 0 < y && y < map.length) {
            map[x][y] = object;
            for (ClientHandler client : clients) {
                client.out.println("O," + x + "," + y + "," + object);
            }
        }
    }

    public void close() throws IOException {
        if (serverSocket.isClosed()) return;
        for (ClientHandler client : clients) {
            client.out.close();
            client.in.close();
            client.socket.close();
        }
        clients.clear();
        serverSocket.close();
    }

    // ------------------------  ClientHandler ------------------------

    private class ClientHandler {
        private final Socket socket;
        private final BufferedReader in;
        private final PrintWriter out;
        private final Thread waitingThread = new Thread(this::allReady);
        //----------------------------------------------
        private Player player;
        private boolean isAlive = true;
        private boolean ready = false;
        private int power = 1;
        private int bombs = 1;
        private boolean canKickBombs = false;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(this::clientReader).start();
        }

        /**
         * Client Timeline
         */
        public void clientReader() {
            // ----------------------- Login to server ---------------------
            String name;
            try {
                name = in.readLine(); // Client gives its name.
            } catch (IOException ignore) {
                return;
            }

            int id = getNextID(); // Gets the next available server ID.
            if (id == -1) { // Server is full.
                out.write("Server is full");
                return;
            }
            this.player = new Player(id, name);
            //System.out.println("Server - New player joined: " + player);

            // Share information with other clients
            StringBuilder sb = new StringBuilder(this.player.introduction() + ",");
            for (ClientHandler client : clients) {
                sb.append(client.player.introduction()).append(",");
                client.out.println("J," + this.player.introduction());
            }
            this.out.println(sb);

            // -----------------------------------------------------------
            //Congrats, you are now a verified user.
            clients.add(this);
            //As a Member you will now get updates from other Members,
            //and also get notified about new Members.


            // ----------------------- Waiting in Hub ---------------------

            waitingThread.start();
            // Waiting for All Clients to ready-up.
            while (!allReady && !socket.isClosed()) {
                try {
                    Thread.sleep(500); // Suboptimal waiting solution
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Ignore the Error warning, I know what im doing.
            // See function: "allReady()" lower on this page. >:(
            waitingThread.stop();

            // sends map details and start positions to all clients.
            out.println(gameData);

            // ----------------------- Starting game ---------------------
            while (true) {
                try {
                    // Player input
                    String clientMsg = in.readLine();
                    // "!isAlive" Ignores dead players.
                    if (!isAlive || clientMsg == null) continue;
                    switch (clientMsg) {
                        case "W" -> move(0, -1, Direction.Up);
                        case "S" -> move(0, 1, Direction.Down);
                        case "A" -> move(-1, 0, Direction.Left);
                        case "D" -> move(1, 0, Direction.Right);
                        case "B" -> placeBomb(player.getXPos(), player.getYPos(), power);
                    }
                } catch (IOException ignore) {
                    //System.out.println("Server - " + player + " disconnected");
                    break;
                }
            }
            // ----------------------- Client leaves  ---------------------
            System.out.println("Server - " + player + " has left.");
            // bye bye
            clients.remove(this);
            for (ClientHandler client : clients) {
                client.out.println("L," + player.getId());
            }
        }

        public void move(int deltaX, int deltaY, Direction direction) {
            player.setDirection(direction);
            int x = player.getXPos() + deltaX;
            int y = player.getYPos() + deltaY;

            switch (map[x][y]) {
                // Player can move through empty tiles.
                case ' ' -> {
                    player.setXPos(x);
                    player.setYPos(y);
                }
                // Player can also kill themselves, by walking into the blast from a bomb.
                case '2', '3' -> this.isAlive = false;
            }
            // Inform everyone:
            for (ClientHandler client : clients) {
                client.out.println("M," + player.getId() + " " + player.getLocation() + " " + direction);
            }
        }

        public void allReady() {
            temp:
            while (true) {
                try {
                    // This right here, is the reason for the use of "thread.stop()"
                    String msg = in.readLine(); // Stuck and annoying >:(
                    if (msg == null || !msg.equals("r")) continue;
                    ready = !ready;

                    // Is everyone ready?
                    for (ClientHandler client : clients) {
                        if (!client.ready) {
                            continue temp;
                        }
                    }
                    // Yes they are. :)
                    allReady = true;
                    startGame();
                    break;
                } catch (IOException ignore) {
                    break;
                }
            }
        }
    }
}
