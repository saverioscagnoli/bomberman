package core;

import java.awt.image.BufferedImage;
import javax.swing.*;
import entities.Bomberman;
import managers.BombManager;
import managers.EnemyManager;
import managers.MouseManager;
import managers.SoundManager;
import managers.PowerupManager;
import managers.SaveManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;

import java.awt.*;
import util.Consts;
import util.GameState;
import util.Utils;

public class Loop extends JPanel implements Runnable {
  /* The instance for the singleton */
  private static Loop instance = null;
  public boolean isMouseChanging;

  /* The time to update the game loop */
  private static final long TARGET_FRAME_TIME = 1_000_000_000 / 60;

  /* The container of 2 JPanels while playing: The points overlay and the game */
  private static JPanel container;

  /* The separate thread to use for the game loop */
  private Thread thread;

  /* A flag to see if the thread is running */
  private boolean running;

  /* The JPanel that contains points and lives */
  public JPanel overlay;

  /* The main menu background image saved to improve performance */
  private BufferedImage menuBg;
  private BufferedImage menuArrow;
  private BufferedImage statsBg;
  private BufferedImage MouseIcon;
  private BufferedImage MButton;
  public int arrowY;

  /* The elapsed frames from the start of the game */
  public int elapsed;

  /* The current game state. See GameState.java */
  public GameState gameState;

  /* The instance of the player character */
  public Bomberman bomberman;

  /* The managers to use, saved so we don't have to build them every time */
  public TileManager tileManager;
  public BombManager bombManager;
  public EnemyManager enemyManager;
  public PowerupManager powerupManager;
  public SoundManager musicManager;

  private Controller controller;
  private MenuHandler menuHandler;
  public Sprite buttonToggle;

  /*
   * constructor. This will be called only the first time the loop is
   * built
   */
  private Loop(JPanel c) {
    /* Set all the variables to their initial state */
    container = c;
    this.gameState = GameState.Menu;
    this.menuBg = Utils.loadImage("assets/title-screen.png");
    this.menuArrow = Utils.loadImage("assets/menu-arrow.png");
    this.statsBg = Utils.loadImage("assets/Stats.png");
    this.arrowY = 555;
    this.elapsed = 0;
    this.running = false;

    /* Build all the managers */
    this.tileManager = TileManager.build();
    this.bombManager = BombManager.build();
    this.enemyManager = EnemyManager.build();
    this.powerupManager = PowerupManager.build();
    this.musicManager = SoundManager.build();
    this.bomberman = new Bomberman(50, 50);
    this.MouseIcon = Utils.loadImage("assets/MouseIcon.png");
    this.MButton = Utils.loadImage("assets/MButton.png");

    /* Set the panel */
    this.setPreferredSize(new Dimension(Consts.screenWidth, Consts.screenHeight));
    this.setFocusable(true);

    /* Create the main menu */
    this.createMainMenu();
    this.enemyManager.instanciateEnemies(5);

    this.controller = Controller.build(this);
    this.menuHandler = MenuHandler.build(this);
    this.addKeyListener(this.menuHandler);
    this.buttonToggle = new Sprite("buttonToggle", 2, 2, "redButton", new SpriteAnimation[] {
        new SpriteAnimation("redButton", 2, 0, 5),
        new SpriteAnimation("greenButton", 2, 1, 5)
    }, 1);
    this.isMouseChanging = false;
  }

  public static Loop build() {
    return instance;
  }

  /* The function to call the second constructor. used for singleton. */
  public static Loop build(JPanel c) {
    if (instance == null) {
      instance = new Loop(c);
    }
    return instance;
  }

  /* The function that creates the overlay with points and lives */
  private void createOverlay() {
    JPanel overlay = new JPanel();
    overlay.setPreferredSize(new Dimension(Consts.screenWidth, 120));
    container.add(overlay, BorderLayout.NORTH);
    overlay.setFont(Utils.loadFont(60f));
    overlay.setBackground(Color.decode("#FC8400"));
    this.overlay = overlay;
  }

  /* The function that creates the main menu */
  private void createMainMenu() {
    this.removeAll();
    this.revalidate();
    this.repaint();
  }

  /* The function that takes in a new state and replaces the old one */
  public void setState(GameState state) {
    this.gameState = state;
    this.updateState();
  }

  /* The function that takes care of what happens when updating the state */
  public void updateState() {
    this.removeAll();

    switch (this.gameState) {
      case Menu: {
        this.createMainMenu();
        break;
      }
      case Stats: {
        SoundManager.build().ost(gameState);
        break;
      }
      case InGame: {
        MouseManager.build();
        if (this.bomberman.immune) {
          Utils.setTimeout(() -> this.bomberman.immune = false, 15000);
        }
        /* Start the thread and resume all the bombs */
        this.start();
        this.createOverlay();
        this.bombManager.resumeBombs();
        break;
      }
      case Pause: {
        /* Stop the thread and pause all the bombs */
        this.stop();
        this.bombManager.pauseBombs();
        break;
      }
      default: {
        break;
      }
    }

    this.revalidate();
    this.repaint();

    /* Play the music according to the game state */
    this.musicManager.ost(this.gameState);
  }

  public void addController() {
    this.addKeyListener(this.controller);
  }

  public void removeController() {
    this.removeKeyListener(this.controller);
  }

  /* The function that takes care of creating the thread and starting it */
  private void start() {
    this.addController();
    this.running = true;
    this.thread = new Thread(this);
    this.thread.start();
  }

  /* The function that takes care of stopping the thread */
  public void stop() {
    this.removeController();
    this.running = false;
    try {
      this.thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /* The game loop. */
  @Override
  public void run() {
    long currentTime = System.nanoTime();
    long accumulator = 0L;

    while (this.running) {
      long newTime = System.nanoTime();
      long frameTime = newTime - currentTime;

      // Limit frame time to avoid spiraling frame time in case of slowdowns
      if (frameTime > 250_000_000) {
        frameTime = 250_000_000;
      }

      currentTime = newTime;
      accumulator += frameTime;

      while (accumulator >= TARGET_FRAME_TIME) {
        this.update();
        accumulator -= TARGET_FRAME_TIME;
      }

      this.repaint();

      // Sleep to control frame rate
      long sleepTime = (TARGET_FRAME_TIME - (System.nanoTime() - currentTime)) / 1_000_000;
      if (sleepTime > 0) {
        try {
          Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /* The function that updates the game logic and sprites */
  private void update() {
    this.elapsed++;
    bomberman.update(elapsed);
    if (MouseManager.build().tileClicked != null && MouseManager.build().enabled) {
      MouseManager.build().checkPositionReached();
    }
    tileManager.updateTiles(elapsed);
    bombManager.updateBombs(elapsed);
    bombManager.updateExplosions(elapsed);
    enemyManager.updateEnemies(elapsed);
    powerupManager.updatePowerup(elapsed);

    if (this.isMouseChanging) {
      this.buttonToggle.update(elapsed);
      if (this.buttonToggle.currentAnimation.name == "redButton") {
        if (this.buttonToggle.current == 0 && this.buttonToggle.cycles > 0) {
          this.buttonToggle.cycles = 0;
          this.buttonToggle.setAnimation("greenButton");
          this.isMouseChanging = false;
        }
      } else {
        if (this.buttonToggle.current == 0 && this.buttonToggle.cycles > 0) {
          this.buttonToggle.cycles = 0;
          this.buttonToggle.setAnimation("redButton");
          this.isMouseChanging = false;
        }
      }
    }
  }

  /* The function that renders everything needed on screen */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    /* Render according to the game state */
    switch (this.gameState) {
      case Menu: {
        g2d.drawImage(this.menuBg, 0, 0, this.getWidth(), this.getHeight(), null);
        g2d.drawImage(this.menuArrow, 190, this.arrowY, 30, 50, null);
        break;
      }
      case Stats: {
        g2d.drawImage(this.statsBg, 0, 0, this.getWidth(), this.getHeight(), null);
        System.out.println(SaveManager.readProgress().get("wins"));
        g2d.setFont(Utils.loadFont(60f));
        g2d.setColor(Color.BLACK);
        g2d.drawString("" + SaveManager.readProgress().get("losses"), this.getWidth() - 210, 234);
        g2d.drawString("" + SaveManager.readProgress().get("wins"), this.getWidth() - 160, 380);
        g2d.drawString("" + SaveManager.readProgress().get("score"), this.getWidth() - 160 - 297, 380 + 200);
        BufferedImage credits = Utils.loadImage("assets/guys.png");
        g2d.drawImage(credits, 223, 643, credits.getWidth() * 3, credits.getHeight() * 3, null);
        break;
      }
      case InGame:
      case Pause: {
        /* Draw everything the game needs */
        tileManager.drawBasic(g2d);
        if (MouseManager.build().enabled) {
          MouseManager.build().drawLegalPositions(g2d);
        }
        tileManager.drawWalls(g2d);
        bombManager.drawBombs(g2d);
        bombManager.drawExplosions(g2d);
        enemyManager.drawEnemies(g2d);
        bomberman.render(g2d);
        powerupManager.drawPowerups(g2d);

        /* Draw the overlay */
        Graphics2D og2d = (Graphics2D) this.overlay.getGraphics();
        Color orangeColor = Color.decode("#FC8400");
        og2d.setColor(orangeColor);
        if (this.gameState == GameState.Pause) {
          /* Draw a "pause" when the game is paused */
          int x = (int) (this.overlay.getWidth() * 0.5);
          int y = (int) (this.overlay.getHeight() * 0.5);
          og2d.setColor(Color.BLACK);
          og2d.drawString("PAUSE!", x - 90, y + 15);
        } else {

          /* Draw the points and lives */
          og2d.setColor(Color.BLACK);
          og2d.drawString("" + this.bomberman.lives, 50, 75);
          int scoreLength = String.valueOf(this.bomberman.score).length();
          og2d.drawString("" + this.bomberman.score, this.getWidth() - 75 - scoreLength * 33, 75);
        }
        this.buttonToggle.draw(og2d, 100, 35, 100, 100);
        og2d.drawImage(this.MButton, 115, 0, MButton.getWidth() * 2, MButton.getHeight() * 2, null);
        og2d.drawImage(this.MouseIcon, 180, 15, MouseIcon.getWidth() * 2, MouseIcon.getHeight() * 2, null);
        /* Draw the line at the bottom of the overlay to separate the game */
        int x1 = 0;
        int x2 = this.overlay.getWidth();
        int y1 = this.overlay.getHeight();
        int y2 = y1;

        og2d.setStroke(new BasicStroke(10));
        og2d.setColor(Color.BLACK);
        og2d.drawLine(x1, y1, x2, y2);

        og2d.dispose();
        break;
      }
      default: {
        break;
      }
    }
    g2d.dispose();
  }
}
