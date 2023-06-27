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
  private static Loop instance = null;
  private static final long TARGET_FRAME_TIME = 1_000_000_000 / 60;
  private static JPanel container;
  private Thread thread;
  private boolean running;
  private boolean added;

  private BufferedImage menuBg;
  private int elapsed;

  public GameState gameState;
  public Bomberman bomberman;
  public TileManager tileManager;
  public BombManager bombManager;
  public EnemyManager enemyManager;
  public PowerupManager powerupManager;
  public SoundManager musicManager;

  private Loop() {
    this.gameState = GameState.Menu;
    this.menuBg = Utils.loadImage("assets/title-screen.png");
    this.elapsed = 0;
    this.running = false;
    this.added = false;

    this.tileManager = TileManager.build();
    this.bombManager = BombManager.build();
    this.enemyManager = EnemyManager.build();
    this.powerupManager = PowerupManager.build();
    this.musicManager = SoundManager.build();
    this.bomberman = new Bomberman(50, 50);

    this.setPreferredSize(new Dimension(Consts.screenWidth, Consts.screenHeight));
    this.setFocusable(true);
    this.createMainMenu();
  }

  private Loop(JPanel c) {
    container = c;
    this.gameState = GameState.Menu;
    this.menuBg = Utils.loadImage("assets/title-screen.png");
    this.elapsed = 0;
    this.running = false;
    this.added = false;

    this.tileManager = TileManager.build();
    this.bombManager = BombManager.build();
    this.enemyManager = EnemyManager.build();
    this.powerupManager = PowerupManager.build();
    this.musicManager = SoundManager.build();
    this.bomberman = new Bomberman(50, 50);

    this.setPreferredSize(new Dimension(Consts.screenWidth, Consts.screenHeight));
    this.setFocusable(true);
    this.createMainMenu();
  }

  public static Loop build() {
    if (instance == null) {
      instance = new Loop();
    }
    return instance;
  }

  public static Loop build(JPanel c) {
    if (instance == null) {
      instance = new Loop(c);
    }
    return instance;
  }

  private void createOverlay() {
    JPanel overlay = new JPanel();
    overlay.setPreferredSize(new Dimension(Consts.screenWidth, 150));
    overlay.setBackground(Color.BLACK);
    container.add(overlay, BorderLayout.NORTH);

  }

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

  public void setState(GameState state) {
    this.gameState = state;
    this.updateState();
  }

  public void updateState() {
    this.removeAll();

    switch (this.gameState) {
      case Menu: {
        this.createMainMenu();
        break;
      }
      case InGame: {
        this.start();
        this.createOverlay();
        break;
      }
      case Pause: {
        this.stop();
        break;
      }
    }

    this.revalidate();
    this.repaint();
    this.musicManager.ost(this.gameState);
  }

  private void start() {
    if (!this.added) {
      this.addKeyListener(Controller.build());
      this.added = true;
    }
    this.running = true;
    this.thread = new Thread(this);
    this.thread.start();
  }

  public void stop() {
    this.running = false;
    try {
      this.thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

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
        update();
        accumulator -= TARGET_FRAME_TIME;
      }

      repaint();

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

  private void update() {
    this.elapsed++;
    bomberman.update(elapsed);
    tileManager.updateTiles(elapsed);
    bombManager.updateBombs(elapsed);
    bombManager.updateExplosions(elapsed);
    enemyManager.updateEnemies(elapsed);
    PowerupManager.UpdatePowerup(elapsed);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.clearRect(0, 0, getWidth(), getHeight());

    switch (this.gameState) {
      case Menu: {
        g2d.drawImage(this.menuBg, 0, 0, this.getWidth(), this.getHeight(), null);
        break;
      }
      case InGame:
      case Pause: {
        tileManager.drawObstacles(g2d);
        tileManager.drawBasicTiles(g2d);
        bomberman.render(g2d);
        bombManager.drawBombs(g2d);
        bombManager.drawExplosions(g2d);
        enemyManager.drawEnemies(g2d);
        PowerupManager.RenderPowerup(g2d);

        // draw player lives number in top left corner
        g2d.setColor(Color.BLACK);
        g2d.drawString("Lives: " + bomberman.lives, 10, 20);
        g2d.dispose();
        break;
      }
    }
  }
}
