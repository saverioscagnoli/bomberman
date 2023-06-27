package core;

import java.awt.image.BufferedImage;
import javax.swing.*;
import entities.Bomberman;
import managers.BombManager;
import managers.EnemyManager;
import managers.SoundManager;
import managers.PowerupManager;
import managers.TileManager;
import java.awt.*;
import util.Consts;
import util.GameState;
import util.Utils;

public class Loop extends JPanel implements Runnable {
  /* The instance for the singleton */
  private static Loop instance = null;

  /* The time to update the game loop */
  private static final long TARGET_FRAME_TIME = 1_000_000_000 / 60;

  /* The container of 2 JPanels while playing: The points overlay and the game */
  private static JPanel container;

  /* The separate thread to use for the game loop */
  private Thread thread;

  /* A flag to see if the thread is running */
  private boolean running;

  /* A flag to check if the controller has been added to the Loop */
  private boolean added;

  /* The JPanel that contains points and lives */
  private JPanel overlay;

  /* The main menu background image saved to improve performance */
  private BufferedImage menuBg;

  /* The elapsed frames from the start of the game */
  private int elapsed;

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

  /* First constructor */
  private Loop() {
    /* Set all the variables to their initial states */
    this.gameState = GameState.Menu;
    this.menuBg = Utils.loadImage("assets/title-screen.png");
    this.elapsed = 0;
    this.running = false;
    this.added = false;

    /* Build all the managers */
    this.tileManager = TileManager.build();
    this.bombManager = BombManager.build();
    this.enemyManager = EnemyManager.build();
    this.powerupManager = PowerupManager.build();
    this.musicManager = SoundManager.build();

    /* Create the player character */
    this.bomberman = new Bomberman(50, 50);

    /* Set the panel */
    this.setPreferredSize(new Dimension(Consts.screenWidth, Consts.screenHeight));
    this.setFocusable(true);

    /* Create the main menu */
    this.createMainMenu();
  }

  /*
   * Second constructor. This will be called only the first time the loop is
   * built
   */
  private Loop(JPanel c) {
    /* Set all the variables to their initial state */
    container = c;
    this.gameState = GameState.Menu;
    this.menuBg = Utils.loadImage("assets/title-screen.png");
    this.elapsed = 0;
    this.running = false;
    this.added = false;

    /* Build all the managers */
    this.tileManager = TileManager.build();
    this.bombManager = BombManager.build();
    this.enemyManager = EnemyManager.build();
    this.powerupManager = PowerupManager.build();
    this.musicManager = SoundManager.build();
    this.bomberman = new Bomberman(50, 50);

    /* Set the panel */
    this.setPreferredSize(new Dimension(Consts.screenWidth, Consts.screenHeight));
    this.setFocusable(true);

    /* Create the main menu */
    this.createMainMenu();
  }

  /* The function to call the first constructor. used for singleton. */
  public static Loop build() {
    if (instance == null) {
      instance = new Loop();
    }
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

    JButton startButton = new JButton("Start");
    startButton.setBounds(0, 0, 100, 50);

    startButton.addActionListener(e -> {
      this.setState(GameState.InGame);
    });

    this.setLayout(null);
    this.add(startButton);

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
      case InGame: {
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
    }

    this.revalidate();
    this.repaint();

    /* Play the music according to the game state */
    this.musicManager.ost(this.gameState);
  }

  /* The function that takes care of creating the thread and starting it */
  private void start() {
    if (!this.added) {
      this.addKeyListener(Controller.build());
      this.added = true;
    }
    this.running = true;
    this.thread = new Thread(this);
    this.thread.start();
  }

  /* The function that takes care of stopping the thread */
  public void stop() {
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
    tileManager.updateTiles(elapsed);
    bombManager.updateBombs(elapsed);
    bombManager.updateExplosions(elapsed);
    enemyManager.updateEnemies(elapsed);
    PowerupManager.UpdatePowerup(elapsed);
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
        break;
      }
      case InGame:
      case Pause: {
        /* Draw everything the game needs */
        tileManager.drawObstacles(g2d);
        tileManager.drawBasicTiles(g2d);
        bomberman.render(g2d);
        bombManager.drawBombs(g2d);
        bombManager.drawExplosions(g2d);
        enemyManager.drawEnemies(g2d);
        PowerupManager.RenderPowerup(g2d);

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
          og2d.drawString("" + this.bomberman.score, this.getWidth() - 75, 75);
        }

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
    }
    g2d.dispose();
  }
}
