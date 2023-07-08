classDiagram
direction BT
class AnimationManager {
  - AnimationManager() 
  + build() AnimationManager
  + getSprite(String) BufferedImage
  - buildspriteMap() void
}
class Bomb {
  + Bomb(int, int, int) 
  + render(Graphics2D) void
  + dieNotExplode() void
  + explode() void
  + resume() void
  + pause() void
  + die() void
  + update(int) void
   long elapsedTime
}
class BombManager {
  - BombManager() 
  + drawExplosions(Graphics2D) void
  + drawBombs(Graphics2D) void
  + pauseBombs() void
  + build() BombManager
  + addBomb(Bomb) void
  + updateExplosions(int) void
  + updateBombs(int) void
  + resumeBombs() void
  + addExplosion(Explosion) void
}
class BombPowerup {
  + BombPowerup(int, int) 
  + onPickup() void
}
class Bomberman {
  + Bomberman(int, int) 
  + die() void
  + placeBomb() void
  + update(int) void
  + render(Graphics2D) void
  - win() void
}
class ClownBullet {
  + ClownBullet(int, int, int) 
  + render(Graphics2D) void
  + update(int) void
  + die() void
}
class ClownMask {
  + ClownMask(int, int) 
  + update(int) void
  + render(Graphics2D) void
  + die() void
  - move() void
  - spawnBullets() void
  - drawBullets(Graphics2D) void
  - updateBullets(int) void
}
class CollisionChecker {
  - CollisionChecker() 
  + update_Centered_Collisions() void
  + build() CollisionChecker
  + Collision_To_check() void
}
class Consts {
  + Consts() 
}
class Controller {
  - Controller(Loop) 
  + build(Loop) Controller
  + keyPressed(KeyEvent) void
  + keyReleased(KeyEvent) void
  + keyTyped(KeyEvent) void
}
class Cuppen {
  + Cuppen(int, int) 
  + update(int) void
  + render(Graphics2D) void
}
class DebugWindow {
  + DebugWindow() 
}
class Denkyun {
  + Denkyun(int, int) 
  + update(int) void
  + render(Graphics2D) void
  - collide() boolean
}
class DrawPane {
  + DrawPane() 
  + paintComponent(Graphics) void
  + start() void
  + run() void
}
class Enemy {
  + Enemy(int, int, int, int, int, Sprite) 
  + update(int) void
  + render(Graphics2D) void
  + die() void
  + dealDamage(int) void
}
class EnemyManager {
  - EnemyManager() 
  + instanciateEnemies(int) void
  + drawEnemies(Graphics2D) void
  + build() EnemyManager
  + updateEnemies(int) void
}
class Entity {
  + Entity(int, int, int, int, int, Sprite) 
  + die() void
  + render(Graphics2D) void
  + update(int) void
}
class Explosion {
  + Explosion(int, int, String, int) 
  + update(int) void
  + render(Graphics2D) void
  + die() void
}
class FaralsBoss {
  + FaralsBoss(int, int) 
  + die() void
  + render(Graphics2D) void
  + update(int) void
  - spawnBombs() void
  - move() void
}
class GameState {
<<enumeration>>
  - GameState() 
  + valueOf(String) GameState
  + values() GameState[]
}
class LevelManager {
  - LevelManager() 
  + build() LevelManager
  + loadLevel(int) void
  + reloadLevel() void
  + loadNextLevel() void
}
class LivesPowerup {
  + LivesPowerup(int, int) 
  + onPickup() void
}
class Loop {
  - Loop(JPanel) 
  + updateState() void
  + removeController() void
  + build() Loop
  + build(JPanel) Loop
  + addController() void
  + run() void
  + stop() void
  # paintComponent(Graphics) void
  - createMainMenu() void
  - update() void
  - createOverlay() void
  - start() void
   GameState state
}
class Main {
  ~ Main() 
  + main(String[]) void
}
class MenuHandler {
  - MenuHandler(Loop) 
  + build(Loop) MenuHandler
  + keyReleased(KeyEvent) void
  + keyTyped(KeyEvent) void
  + keyPressed(KeyEvent) void
  - setArrow() void
}
class MouseManager {
  ~ MouseManager() 
  + findLegalPositions(int[]) void
  + checkPositionReached() void
  + build() MouseManager
  + drawLegalPositions(Graphics2D) void
}
class NutsStar {
  + NutsStar(int, int) 
  + render(Graphics2D) void
  + update(int) void
  - collide() boolean
  - handleCollisions(String[], boolean, Runnable) void
   String direction
}
class Pakupa {
  + Pakupa(int, int) 
  + render(Graphics2D) void
  + update(int) void
  - checkBombs() void
  - collide() boolean
}
class PassThroughPowerup {
  + PassThroughPowerup(int, int) 
  + onPickup() void
}
class PowerUp {
  + PowerUp(String, int, int, Sprite) 
  + die() void
  + render(Graphics2D) void
  + onPickup() void
  + update(int) void
}
class PowerupManager {
  - PowerupManager() 
  + drawPowerups(Graphics2D) void
  + updatePowerup(int) void
  + build() PowerupManager
}
class Puropen {
  + Puropen(int, int) 
  + update(int) void
  + render(Graphics2D) void
  - collide() boolean
}
class RadiusPowerup {
  + RadiusPowerup(int, int) 
  + onPickup() void
}
class RainPowerup {
  + RainPowerup(int, int) 
  + onPickup() void
}
class SaveManager {
  + SaveManager() 
  + incrementLosses() void
  + writeProgress(HashMap~String, String~) void
  + resetLevel() void
  + incrementWins() void
  + readProgress() HashMap~String, String~
  + incrementLevel() void
  + incrementScore(int) void
}
class SkullPowerup {
  + SkullPowerup(int, int) 
  + onPickup() void
}
class SoundManager {
  - SoundManager() 
  + build() SoundManager
  + ost(GameState) void
}
class SpeedPowerup {
  + SpeedPowerup(int, int) 
  + onPickup() void
}
class Sprite {
  + Sprite(String, double, int, String, SpriteAnimation[], float) 
  + update(int) void
  + draw(Graphics2D, double, double, int[]) void
   String animation
}
class SpriteAnimation {
  + SpriteAnimation(String, int, int, int) 
}
class Tile {
  + Tile(int, int, boolean, boolean, boolean, Sprite) 
  + Tile(int, int, boolean, String) 
  + Tile(int, int) 
  + update(int) void
  + render(Graphics2D) void
  + die() void
}
class TileManager {
  - TileManager() 
  + build() TileManager
  + updateTiles(int) void
  + drawBasic(Graphics2D) void
  + drawWalls(Graphics2D) void
  - setHatch() void
  - setTiles() void
  - readGrid() void
}
class TileType {
<<enumeration>>
  - TileType() 
  + values() TileType[]
  + valueOf(String) TileType
}
class Utils {
  + Utils() 
  + normalizeEntityPos(Entity) int[]
  + loadFont(float) Font
  + playSound(String) Clip
  + rng(int, int) int
  + createDebugWindow() void
  + setTimeout(Runnable, int) void
  + normalizePos(int, int) int[]
  + loadImage(String) BufferedImage
  + enemyCollision(Enemy, String) boolean
  + readLevel(String) TileType[][]
  + copyImage(BufferedImage) BufferedImage
  + pick(T[]) T
   String[] levelNames
}
class VestPowerup {
  + VestPowerup(int, int) 
  + onPickup() void
}

Bomb  -->  Entity 
BombManager "1" *--> "bombs *" Bomb 
BombManager "1" *--> "explosions *" Explosion 
BombPowerup  -->  PowerUp 
Bomberman  -->  Entity 
Bomberman "1" *--> "prevTile 1" TileType 
ClownBullet  -->  Entity 
ClownMask "1" *--> "bullets *" ClownBullet 
ClownMask  -->  Enemy 
CollisionChecker "1" *--> "loop_Bomberman 1" Bomberman 
CollisionChecker "1" *--> "SolidTiles *" TileType 
Controller "1" *--> "bomberman 1" Bomberman 
Cuppen  -->  Enemy 
Cuppen  ..>  Sprite : «create»
Cuppen  ..>  SpriteAnimation : «create»
Denkyun  -->  Enemy 
Enemy  -->  Entity 
Enemy "1" *--> "prevTile 1" TileType 
EnemyManager "1" *--> "enemies *" Enemy 
Entity "1" *--> "sprite 1" Sprite 
Explosion  -->  Entity 
FaralsBoss  -->  Enemy 
LevelManager  ..>  ClownMask : «create»
LevelManager  ..>  FaralsBoss : «create»
LivesPowerup  -->  PowerUp 
Loop "1" *--> "bombManager 1" BombManager 
Loop "1" *--> "bomberman 1" Bomberman 
Loop "1" *--> "controller 1" Controller 
Loop "1" *--> "enemyManager 1" EnemyManager 
Loop "1" *--> "gameState 1" GameState 
Loop "1" *--> "menuHandler 1" MenuHandler 
Loop "1" *--> "powerupManager 1" PowerupManager 
Loop "1" *--> "musicManager 1" SoundManager 
Loop "1" *--> "buttonToggle 1" Sprite 
Loop "1" *--> "tileManager 1" TileManager 
MenuHandler "1" *--> "loop 1" Loop 
MouseManager "1" *--> "bomberman 1" Bomberman 
MouseManager "1" *--> "gameGrid *" TileType 
NutsStar  -->  Enemy 
Pakupa  -->  Enemy 
PassThroughPowerup  -->  PowerUp 
PowerUp  -->  Entity 
PowerUp "1" *--> "prevTile 1" TileType 
PowerupManager "1" *--> "powerups *" PowerUp 
Puropen  -->  Enemy 
RadiusPowerup  -->  PowerUp 
RainPowerup  -->  PowerUp 
SkullPowerup  -->  PowerUp 
SpeedPowerup  -->  PowerUp 
Sprite "1" *--> "animations *" SpriteAnimation 
Tile  -->  Entity 
TileManager "1" *--> "walls *" Tile 
TileManager "1" *--> "grid *" TileType 
VestPowerup  -->  PowerUp 
