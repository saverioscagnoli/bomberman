package ui;

public class Button {
  public final String label;
  public final int x;
  public final int y;
  public final int offsetY;
  public final int width;
  public final int height;
  public final String uuid;

  public Button(String uuid, String label, int x, int y, int offsetY, int w, int h) {
    this.uuid = uuid;
    this.label = label;
    this.x = x;
    this.y = y;
    this.offsetY = offsetY;
    this.width = w;
    this.height = h;
  }
}
