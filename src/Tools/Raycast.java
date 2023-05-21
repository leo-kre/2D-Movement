package Tools;

import java.awt.*;

public class Raycast {

    private static final int raycastWidth = 2;
    public boolean Hit(int _x, int _y, int _length, Rectangle _rect) {
        return new Rectangle(_x, _y, raycastWidth, _length).intersects(_rect);
    }
}
