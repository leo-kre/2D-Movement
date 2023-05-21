package Player;

import GameScene.Building;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.*;

public class PlayerPrefab{

    public float x = 100, y = 100;
    public int height = 75;
    public int width = 50;

    public float velocityX;
    public float velocityY;

    public String name = "[No Name]";
    public Color color;

    public String animationType;
    public String animationIndex;

    //boolean
    public boolean isMoving;
    public boolean isGrounded;
    public boolean isJumping;
    public boolean isDashing;
    public boolean isWallRunning;

    public boolean affectedByGravity = true;
    public boolean canMove = true;
    public boolean canJump = false;

    public boolean shouldWallRun = false;

    public float lastYPositionOnGround;
    public float lastXPositionBeforeDash;
    public int jumpCount;
    public int dashCount;

    public Rectangle rectOfWallRunBuilding;

    //movement + collision
    public String direction = "";
    public boolean left = false;
    public boolean right = false;
    public boolean up = false;
    public boolean down = false;

    public void Init(String _name, int _x, int _y, Color _color) {
        x = _x;
        y = _y;
        name = _name;
        color = _color;
    }

}
