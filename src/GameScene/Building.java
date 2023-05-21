package GameScene;

import Main.GameHandler;

import java.awt.*;

public class Building {

    public Rectangle rect;
    public Color color;
    public boolean isWallRunnable;

    private int x;
    private int y;
    private int width;
    private int height;

    public void Init(int _x, int _y, int _width, int _height, Color _color, boolean _isWallRunnable) {
        this.x = _x;
        this.y = _y;
        this.width = _width;
        this.height = _height;
        this.rect = new Rectangle(_x * GameHandler.tileSize, _y * GameHandler.tileSize, _width * GameHandler.tileSize, _height * GameHandler.tileSize);
        this.color = _color;
        this.isWallRunnable = _isWallRunnable;
        GameHandler.buildingList.add(this);
    }

    public void Scale() {
        this.rect = new Rectangle(x * GameHandler.tileSize, y * GameHandler.tileSize, width * GameHandler.tileSize, height * GameHandler.tileSize);
    }
}
