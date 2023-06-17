package managers;

import java.awt.Graphics2D;
import java.util.ArrayList;

import entities.Obstacle;
import util.Consts;
import util.Utils;

public class TileManager {
    public static TileManager instance = null;
    public String[][] grid;
    public ArrayList<Obstacle> tiles;

    private TileManager() {
        int rows = (int) Consts.screenWidth / Consts.tileDims;
        int cols = (int) Consts.screenHeight / Consts.tileDims;
        this.grid = new String[cols][rows];
        this.tiles = new ArrayList<>();
        this.setTiles();
    }

    public static synchronized TileManager getInstance() {
        if (instance == null) {
            instance = new TileManager();
        }
        return instance;
    }

    private void setTiles() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                int x = j * Consts.tileDims;
                int y = i * Consts.tileDims;
                boolean shouldSpawnObstacle = Utils.rng(1, 6) == 3;
                if (i == 0 || i == this.grid.length - 1 || j == 0 || j == this.grid[i].length - 1
                        || (i % 2 == 0 && j % 2 == 0)) {
                    this.grid[i][j] = "W";
                    Obstacle tile = new Obstacle(x, y, true, false, "/assets/wall.png");
                    this.tiles.add(tile);
                } else if (shouldSpawnObstacle) {
                    this.grid[i][j] = "WD";
                    Obstacle tile = new Obstacle(x, y, true, true, "/assets/wall-destructable.png");
                    this.tiles.add(tile);
                } else {
                    this.grid[i][j] = "N";
                    Obstacle tile = new Obstacle(x, y, false, false, "/assets/basic-tile.png");
                    this.tiles.add(tile);
                }
            }
        }
    }

    public void updateTiles() {
        ArrayList<Obstacle> toRemove = new ArrayList<>();
        for (Obstacle tile : this.tiles) {
            if (tile.dead) {
                toRemove.add(tile);
            } else {
                tile.update();
            }
        }
        toRemove.forEach((t) -> this.tiles.remove(t));
    }

    public void drawTiles(Graphics2D g2d) {
        for (Obstacle tile : this.tiles) {
            tile.render(g2d);
        }
    }
}
