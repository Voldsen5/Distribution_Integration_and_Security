package Game2022.Server;

public class Bomb {
    private final int x;
    private final int y;
    private final int power;
    private double fuse;
    private boolean exploded = false;
    private static int v = 0;
    private int i;

    public Bomb(int x, int y, int power, double fuse) {
        this.x = x;
        this.y = y;
        this.power = power;
        this.fuse = fuse;
        i = v++;
    }

    public void run() {
        new Thread(() -> {
            double seconds = 1.75;
            try {
                Thread.sleep((long) (1000 * seconds)); // Fuse
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            explode();
        }).start();
    }

    public void explode() {
        if (this.exploded) return;
        this.exploded = true;
        // --------------------------- Explode ---------------------------------
        // Game Logic:
        blast_wave:// Right
        for (int dx = x + 1; dx <= x + power; dx++) {
            switch (Server.map[dx][y]) {
                case 'w':
                    break blast_wave;
                case 'r':
                    Server.updateTile(dx, y, '7');
                    break blast_wave;
                case '0':
                    Bomb bomb = Server.getBomb(dx, y);
                    if (bomb == null) continue;
                    Server.updateTile(dx, y, '1');
                    bomb.explode();
                    continue;
            }
            Server.killAt(dx, y);
            Server.updateTile(dx, y, '2');
        }
        blast_wave:// Left
        for (int dx = x - 1; dx >= x - power; dx--) {
            switch (Server.map[dx][y]) {
                case 'w':
                    break blast_wave;
                case 'r':
                    Server.updateTile(dx, y, '6');
                    break blast_wave;
                case '0':
                    Bomb bomb = Server.getBomb(dx, y);
                    if (bomb == null) continue;
                    Server.updateTile(dx, y, '1');
                    bomb.explode();
                    continue;
            }
            Server.killAt(dx, y);
            Server.updateTile(dx, y, '2');
        }
        blast_wave:// Down
        for (int dy = y + 1; dy <= y + power; dy++) {
            switch (Server.map[x][dy]) {
                case 'w':
                    break blast_wave;
                case 'r':
                    Server.updateTile(x, dy, '5');
                    break blast_wave;
                case '0':
                    Bomb bomb = Server.getBomb(x, dy);
                    if (bomb == null) continue;
                    Server.updateTile(x, dy, '1');
                    bomb.explode();
                    continue;
            }
            Server.killAt(x, dy);
            Server.updateTile(x, dy, '3');
        }
        blast_wave:// Up
        for (int dy = y - 1; dy >= y - power; dy--) {
            switch (Server.map[x][dy]) {
                case 'w':
                    break blast_wave;
                case 'r':
                    Server.updateTile(x, dy, '4');
                    break blast_wave;
                case '0':
                    Bomb bomb = Server.getBomb(x, dy);
                    if (bomb == null) continue;
                    Server.updateTile(x, dy, '1');
                    bomb.explode();
                    continue;
                default:
                    Server.killAt(x, dy);
                    Server.updateTile(x, dy, '3');
            }
        }
        Server.updateTile(x, y, '1');// Ground Zero
        Server.killAt(x, y);

        new Thread(this::cleanUp).start();
    }

    private void cleanUp() {
        //-------------- Wait a moment and remove effects -------------
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int x = this.x + 1; x <= this.x + power; x++) {// Right
            if (Server.map[x][y] == 'w' || Server.map[x][y] == 'r') break;
            Server.updateTile(x, y, ' ');
        }
        for (int x = this.x - 1; x >= this.x - power; x--) {// Left
            if (Server.map[x][y] == 'w' || Server.map[x][y] == 'r') break;
            Server.updateTile(x, y, ' ');
        }
        for (int y = this.y + 1; y <= this.y + power; y++) { // Down
            if (Server.map[x][y] == 'w' || Server.map[x][y] == 'r') break;
            Server.updateTile(x, y, ' ');
        }
        for (int y = this.y - 1; y >= this.y - power; y--) {// Up
            if (Server.map[x][y] == 'w' || Server.map[x][y] == 'r') break;
            Server.updateTile(x, y, ' ');
        }
        Server.updateTile(x, y, ' ');// Ground Zero
        Server.bombs.remove(this);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
