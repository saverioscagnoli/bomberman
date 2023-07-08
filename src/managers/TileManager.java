package managers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
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
        // this.setTiles();
        this.grid = Utils.readLevel("levels/level-1.lvl");
        this.readGrid();
    }

    /* Singleton */
    public static synchronized TileManager build() {
        if (instance == null) {
            instance = new TileManager();
        }
        return instance;
    }

    public void readGrid() {
        this.basicTiles.clear();
        this.walls.clear();
        this.hatch = null;
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                int x = j * Consts.tileDims;
                int y = i * Consts.tileDims;
                TileType tileAt = this.grid[i][j];
                Tile tile = null;
                String src = null;

                switch (tileAt) {
                    case Wall: {
                        if (i == 0) {
                            if (j == 0)
                                src = "w-tl";
                            else if (j == this.grid[i].length - 1)
                                src = "w-tr";
                            else
                                src = "w-t";
                        } else if (i == this.grid.length - 1) {
                            if (j == 0)
                                src = "w-bl";
                            else if (j == this.grid[i].length - 1)
                                src = "w-br";
                            else
                                src = "w-b";
                        } else if (j == 0)
                            src = "w-l";
                        else if (j == this.grid[i].length - 1)
                            src = "w-r";
                        else
                            src = "w-center";
                        tile = new Tile(x, y, true, src);
                        this.walls.add(tile);
                        break;
                    }
                    case Obstacle: {
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
                        this.walls.add(tile);
                        break;
                    }
                    default: {
                        break;
                    }
                }
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
                this.basicTiles.add(new Tile(x, y, false, src));
            }
        }
        this.setHatch();
    }

    private void setHatch() {
        if (this.walls.stream().filter(w -> w.destructable).count() == 0) {
            this.hatch = new Tile(0, 0);
            return;
        }
        Tile tileWithHatch = (Tile) Utils.pick(this.walls.toArray());
        while (!tileWithHatch.destructable) {
            tileWithHatch = (Tile) Utils.pick(this.walls.toArray());
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
    public void drawWalls(Graphics2D g2d) {
        this.hatch.render(g2d);
        List<Tile> wallsCopy = new ArrayList<>(this.walls);
        for (Tile w : wallsCopy) {
            if (!w.dead) {
                w.render(g2d);
            }
        }
    }

    public void drawBasic(Graphics2D g2d) {
        for (Tile basicTile : this.basicTiles) {
            if (!basicTile.dead) {
                basicTile.render(g2d);
            }
        }
    }
}
