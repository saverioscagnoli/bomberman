package managers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import entities.Tile;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

/* This classe manages the tiles: updating, drawing, animating etc. It also takes care of the creation of the levels */

public class TileManager {
    /* Instance for the singleton */
    public static TileManager instance = null;

    /* A grid of tiles: this is important because it is the "field" for the game */
    public TileType[][] grid;

    /* An arraylist that contains SOLID walls */
    public ArrayList<Tile> walls;

    /* An arraylist that contains only the backround tiles */
    public ArrayList<Tile> basicTiles;

    public Tile hatch;

    private TileManager() {
        /* Set all the initial properties */
        int rows = (int) Consts.screenWidth / Consts.tileDims;
        int cols = (int) Consts.screenHeight / Consts.tileDims;
        this.grid = new TileType[cols][rows];
        this.walls = new ArrayList<>();
        this.basicTiles = new ArrayList<>();

        /* Set the tiles */
        this.setTiles();
    }

    /* Singleton */
    public static synchronized TileManager build() {
        if (instance == null) {
            instance = new TileManager();
        }
        return instance;
    }

    public void addBasicTile(Tile tile) {
        this.basicTiles.add(tile);
    }

    /*
     * This function takes care of genrating the tiles, the onbstacles, and specific
     * tiles like edge tiles or shadow tiles
     */
    private void setTiles() {
        /* Loop through the grid (a matrix) */
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                /* Get the x and y coordinates */
                int x = j * Consts.tileDims;
                int y = i * Consts.tileDims;

                /* Check if we should spawn an obstacle */
                boolean shouldSpawnObstacle = Utils.rng(1, 10) == 5;

                Tile tile = null;
                String src = null;

                /* If it is on the first horizontal iteration, set the top walls */
                if (i == 0) {
                    if (j == 0) {
                        src = "w-tl";
                    } else if (j == this.grid[i].length - 1) {
                        src = "w-tr";
                    } else {
                        src = "w-t";
                    }
                    tile = new Tile(x, y, true, src);
                    this.grid[i][j] = TileType.Wall;
                    this.walls.add(tile);
                    /* If it is on the last horizontal iteration, set the bottom wall */
                } else if (i == this.grid.length - 1) {
                    if (j == 0) {
                        src = "w-bl";
                    } else if (j == this.grid[i].length - 1) {
                        src = "w-br";
                    } else {
                        src = "w-b";
                    }
                    tile = new Tile(x, y, true, src);
                    this.grid[i][j] = TileType.Wall;
                    this.walls.add(tile);
                    /* If any iteration is on an index divisible by 2, set a wall (grid pattern) */
                } else if (i % 2 == 0 && j % 2 == 0 && j != 0 && j < this.grid[i].length - 1) {
                    src = "w-center";
                    tile = new Tile(x, y, true, src);
                    this.grid[i][j] = TileType.Wall;
                    this.walls.add(tile);
                } else {
                    /* On the first vertical iteration, set the left walls */
                    if (j == 0) {
                        src = "w-l";
                        tile = new Tile(x, y, true, src);
                        this.grid[i][j] = TileType.Wall;
                        this.walls.add(tile);
                        /* On the last vertical iteration, set the right walls */
                    } else if (j == this.grid[i].length - 1) {
                        src = "w-r";
                        tile = new Tile(x, y, true, src);
                        this.grid[i][j] = TileType.Wall;
                        this.walls.add(tile);
                    } else {
                        /* If we should spawn an obstacle, set it */
                        if (shouldSpawnObstacle) {
                            src = "wd-1";
                            String animName = null;
                            /*
                             * Set if it is the obstacle sprite at the edge of the map. See:
                             * assets/tiles/idle-edge.png
                             */
                            if (i == 1 || this.grid[i - 1][j] == TileType.Wall) {
                                animName = "idle-edge";
                            } else {
                                animName = "idle";
                                src = "wd-1";
                            }
                            tile = new Tile(x, y, true, false, true, new Sprite(src, 6, 3, animName,
                                    new SpriteAnimation[] {
                                            new SpriteAnimation("idle", 4, 0, 10),
                                            new SpriteAnimation("idle-edge", 4, 1, 10),
                                            new SpriteAnimation("death", 6, 2, 10)
                                    }, 1));
                            this.grid[i][j] = TileType.Obstacle;
                            this.walls.add(tile);
                        } else {
                            /* If it doesn't have anything, then it's an empty tile */
                            this.grid[i][j] = TileType.Empty;
                        }
                    }
                }
                /* Check if it needs to be the edge tile. See assets/tiles/basic-1-edge.png */
                if (i == 1 || (i > 0 && this.grid[i - 1][j] == TileType.Wall)) {
                    src = "basic-1-edge";
                    /*
                     * Check if there is an obstacle on the tile above. If yes, set a shadow tile.
                     * See assets/tiles/basic-1-shadow.png
                     */
                } else if (i > 1 && this.grid[i - 1][j] == TileType.Obstacle) {
                    src = "basic-1-shadow";
                } else {
                    src = "basic-1";
                }
                this.addBasicTile(new Tile(x, y, false, src));
            }
        }

        Tile tileWithHatch = this.walls.get(Utils.rng(0, this.walls.size() - 1));
        while (!tileWithHatch.destructable) {
            tileWithHatch = this.walls.get(Utils.rng(0, this.walls.size() - 1));
        }
        this.hatch = new Tile(tileWithHatch.posX, tileWithHatch.posY);
    }

    /* Update all the tiles at once */
    public void updateTiles(int elapsed) {
        ArrayList<Tile> toRemove = new ArrayList<>();
        this.hatch.update(elapsed);
        for (Tile tile : this.walls) {
            if (tile.destructable) {
                if (tile.dead) {
                    toRemove.add(tile);
                } else {
                    tile.update(elapsed);
                }
            }
        }
        toRemove.forEach((t) -> {
            this.walls.remove(t);
        });
    }

    /* Draw all the tiles at once */
    public void drawBasicTiles(Graphics2D g2d) {
        Iterator<Tile> it = this.walls.iterator();
        this.hatch.render(g2d);
        while (it.hasNext()) {
            Tile walls = it.next();
            if (!walls.dead) {
                walls.render(g2d);
            } else {
                it.remove();
            }
        }
    }

    /* Draw all the obstacles at once */
    public void drawObstacles(Graphics2D g2d) {
        for (Tile basicTile : this.basicTiles) {
            if (!basicTile.dead) {
                basicTile.render(g2d);
            }
        }

    }
}
