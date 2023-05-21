package GameScene;

import Main.Main;
import Tools.Point;

import java.awt.*;
import java.util.ArrayList;

public class ProceduralGeneration {

    public static void Generate(int _width, int _height, int _density, int _growth) {

    }

    private static void CreateTile(int _x, int _y) {
        Building b = new Building();
        b.Init(_x, _y, 1, 1, Color.BLACK, true);
    }
}

