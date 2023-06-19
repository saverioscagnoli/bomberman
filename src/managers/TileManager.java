package managers;

import java.awt.Graphics2D;
import java.util.ArrayList;

import entities.Obstacle;
import ui.SpriteAnimation;
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
                        src = Consts.tilesPath + "w-tl.png";
                    } else if (j == this.grid[i].length - 1) {
                        src = Consts.tilesPath + "w-tr.png";
                    } else {
                        src = Consts.tilesPath + "w-t.png";
                    }
                    tile = new Obstacle(x, y, true, true, false, src);
                    this.grid[i][j] = "W";
                    this.walls.add(tile);
                } else if (i == this.grid.length - 1) {
                    if (j == 0) {
                        src = Consts.tilesPath + "w-bl.png";
                    } else if (j == this.grid[i].length - 1) {
                        src = Consts.tilesPath + "w-br.png";
                    } else {
                        src = Consts.tilesPath + "w-b.png";
                    }
                    tile = new Obstacle(x, y, true, true, false, src);
                    this.grid[i][j] = "W";
                    this.walls.add(tile);
                } else if (i % 2 == 0 && j % 2 == 0 && j != 0 && j < this.grid[i].length - 1) {
                    src = Consts.tilesPath + "w-center.png";
                    tile = new Obstacle(x, y, true, true, false, src);
                    this.grid[i][j] = "W";
                    this.walls.add(tile);
                } else {
                    if (j == 0) {
                        src = Consts.tilesPath + "w-l.png";
                        tile = new Obstacle(x, y, true, true, false, src);
                        this.grid[i][j] = "W";
                        this.walls.add(tile);
                    } else if (j == this.grid[i].length - 1) {
                        src = Consts.tilesPath + "w-r.png";
                        tile = new Obstacle(x, y, true, true, false, src);
                        this.grid[i][j] = "W";
                        this.walls.add(tile);
                    } else {
                        if (shouldSpawnObstacle) {
                            if (i == 1 || this.grid[i - 1][j] == "W") {
                                src = Consts.tilesPath + "wd-1-edge.png";
                            } else {
                                src = Consts.tilesPath + "wd-1.png";
                            }
                            tile = new Obstacle(x, y, true, false, true, src);
                            tile.addAnimation("idle",
                                    new SpriteAnimation(tile.spritesheet, 1, 4, tile.scale, 0, 4, 10));
                            this.grid[i][j] = "WD";
                            this.walls.add(tile);
                        } else {
                            this.grid[i][j] = "N";
                        }
                    }
                }
                if (i == 1 || (i > 0 && this.grid[i - 1][j] == "W")) {
                    src = Consts.tilesPath + "basic-1-edge.png";
                } else {
                    src = Consts.tilesPath + "basic-1.png";
                }
                this.addBasicTile(new Obstacle(x, y, false, true, false, src));
            }
        }
    }

    public void updateTiles() {
        ArrayList<Obstacle> toRemove = new ArrayList<>();
        for (Obstacle tile : this.walls) {
            if (tile.dead) {
                toRemove.add(tile);
            } else {
                tile.update();
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
