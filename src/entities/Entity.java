package entities;

import java.awt.Graphics2D;

import ui.Sprite;

public abstract class Entity {

    /**
     * 
     * Determines if the entity is solid and blocks movement.
     */
    public boolean isSolid = false;
    /**
     * 
     * The x-coordinate of the entity's position.
     */
    public int posX;
    /**
     * 
     * The y-coordinate of the entity's position.
     */
    public int posY;
    /**
     * 
     * The width of the entity.
     */
    public int width;
    /**
     * 
     * The height of the entity.
     */
    public int height;
    /**
     * 
     * The speed of the entity.
     */
    public int speed;
    /**
     * 
     * The sprite associated with the entity.
     */
    public Sprite sprite;
    /**
     * 
     * Indicates if the entity is dead.
     */
    public boolean dead;

    /**
     * 
     * Constructs a new Entity object with the specified parameters.
     * 
     * @param posX   The x-coordinate of the entity's position.
     * @param posY   The y-coordinate of the entity's position.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     * @param speed  The speed of the entity.
     * @param sprite The sprite associated with the entity.
     */
    public Entity(int posX, int posY, int width, int height, int speed, Sprite sprite) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.sprite = sprite;
        this.dead = false;
    }

    /**
     * 
     * Performs the actions required when the entity dies.
     */
    public abstract void die();

    /**
     * 
     * Updates the state of the entity.
     * 
     * @param elapsed The elapsed time since the last update.
     */
    public abstract void update(int elapsed);

    /**
     * 
     * Renders the entity on the specified graphics context.
     * 
     * @param g2d The graphics context to render on.
     */
    public abstract void render(Graphics2D g2d);
}