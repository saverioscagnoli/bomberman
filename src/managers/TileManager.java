package managers;

import java.awt.Graphics2D;
import java.util.ArrayList;

import entities.Obstacle;
import util.Consts;
import util.Utils;

//TODO: ottimizza na cifra

public class TileManager {
    public static TileManager instance = null;
    public String[][] grid;
    public ArrayList<Obstacle> walls;
    public ArrayList<Obstacle> basicTiles;

    private TileManager() {
        int rows = (int) Consts.screenWidth / Consts.tileDims;
        int cols = (int) Consts.screenHeight / Consts.tileDims;
        this.grid = new String[cols][rows];
        this.walls = new ArrayList<>();
        this.basicTiles = new ArrayList<>();
        this.setTiles();
    }

    public static synchronized TileManager getInstance() {
        if (instance == null) {
            instance = new TileManager();
        }
        return instance;
    }

    public void addBasicTile(Obstacle tile) {
        this.basicTiles.add(tile);
    }

    private void setTiles() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                int x = j * Consts.tileDims;
                int y = i * Consts.tileDims;
                boolean shouldSpawnObstacle = Utils.rng(1, 10) == 5;
                Obstacle tile = null;
                String src = null;

                if (i == 0) {
                    if (j == 0) {
                        src = "w-tl";
                    } else if (j == this.grid[i].length - 1) {
                        src = "w-tr";
                    } else {
                        src = "w-t";
                    }
                    tile = new Obstacle(x, y, true, true, false, src, 1, 1, 1);
                    this.grid[i][j] = "W";
                    this.walls.add(tile);
                } else if (i == this.grid.length - 1) {
                    if (j == 0) {
                        src = "w-bl";
                    } else if (j == this.grid[i].length - 1) {
                        src = "w-br";
                    } else {
                        src = "w-b";
                    }
                    tile = new Obstacle(x, y, true, true, false, src, 1, 1, 1);
                    this.grid[i][j] = "W";
                    this.walls.add(tile);
                } else if (i % 2 == 0 && j % 2 == 0 && j != 0 && j < this.grid[i].length - 1) {
                    src = "w-center";
                    tile = new Obstacle(x, y, true, true, false, src, 1, 1, 1);
                    this.grid[i][j] = "W";
                    this.walls.add(tile);
                } else {
                    if (j == 0) {
                        src = "w-l";
                        tile = new Obstacle(x, y, true, true, false, src, 1, 1, 1);
                        this.grid[i][j] = "W";
                        this.walls.add(tile);
                    } else if (j == this.grid[i].length - 1) {
                        src = "w-r";
                        tile = new Obstacle(x, y, true, true, false, src, 1, 1, 1);
                        this.grid[i][j] = "W";
                        this.walls.add(tile);
                    } else {
                        if (shouldSpawnObstacle) {
                            src = "wd-1";
                            String animationName = null;
                            if (i == 1 || this.grid[i - 1][j] == "W") {
                                animationName = "idle-edge";
                            } else {
                                animationName = "idle";
                                src = "wd-1";
                            }
                            tile = new Obstacle(x, y, true, false, true, src, 6, 12, 3);
                            this.grid[i][j] = "WD";
                            tile.setAnimation(animationName);
                            this.walls.add(tile);
                        } else {
                            this.grid[i][j] = "N";
                        }
                    }
                }
                if (i == 1 || (i > 0 && this.grid[i - 1][j] == "W")) {
                    src = "basic-1-edge";
                } else {
                    src = "basic-1";
                }
                this.addBasicTile(new Obstacle(x, y, false, true, false, src, 1, 1, 1));
            }
        }
    }

    public void updateTiles() {
        ArrayList<Obstacle> toRemove = new ArrayList<>();
        for (Obstacle tile : this.walls) {
            if (tile.destructable) {
                if (tile.dead) {
                    toRemove.add(tile);
                } else {
                    tile.update();
                }
            }
        }
        toRemove.forEach((t) -> {
            this.walls.remove(t);
        });
    }

    public void drawBasicTiles(Graphics2D g2d) {
        for (Obstacle tile : this.walls) {
            if (!tile.dead) {
                tile.render(g2d);
            }
        }
    }

    public void drawObstacles(Graphics2D g2d) {
        for (Obstacle basicTile : this.basicTiles) {
            if (!basicTile.dead) {
                basicTile.render(g2d);
            }
        }

    }
}
