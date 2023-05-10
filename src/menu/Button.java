package menu;

public class Button {
  public String label;
  public boolean visible;
  public final int x;
  public final int y;
  public final int width;
  public final int height;
  public String uuid;

  public Button(String uuid, String label, boolean visible, int x, int y, int w, int h) {
    this.uuid = uuid;
    this.visible = visible;
    this.label = label;
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
  }
}
