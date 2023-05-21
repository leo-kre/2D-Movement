package Main;

import GameScene.ProceduralGeneration;
import Player.PlayerPrefab;
import GameScene.Building;

import java.awt.*;
import java.util.ArrayList;

public class GameHandler {
    public static ArrayList<PlayerPrefab> playerList = new ArrayList<PlayerPrefab>();
    public static ArrayList<Building> buildingList = new ArrayList<Building>();

    public static int mapWidth;
    public static int mapHeight;

    public static int tileSize = 50;
    public static final int tileWidth = 38;
    public static final int tileHeight = 21;

    public static void InitPlayer(boolean _isClient, String _name, int _x, int _y, Color _color) {
        if(_isClient) {
            Main.player = new PlayerPrefab();
            Main.player.Init(_name, _x, _y, _color);
        }
    }

    public static void InitGameScene() {

        buildingList.clear();

        CreateMap();

        CreateGameBorder();
        mapWidth = tileWidth * tileSize;
        mapHeight = tileHeight * tileSize;
    }

    public static void CreateMap() {
        CreateTile(10, 5);
    }

    public static void CreateTile(int _x, int _y) {
        Building b = new Building();
        b.Init(_x, _y, 1, 1, Color.BLACK, true);
    }

    public static void CreateGameBorder() {
        Building top = new Building();
        top.Init(0, 0, GameHandler.tileWidth, 1, Color.white, false);

        Building bottom = new Building();
        bottom.Init(0, tileHeight - 1, GameHandler.tileWidth, 1, Color.white, false);

        Building left = new Building();
        left.Init(0, 0, 1, GameHandler.tileHeight - 1, Color.white, true);

        Building right = new Building();
        right.Init(tileWidth - 1, 0, 1, GameHandler.tileHeight, Color.white, true);

        Building b = new Building();
        b.Init(15, 15, 2, 2, Color.black, true);
    }

    public static void UpdateViewport() {
        mapWidth = tileWidth * tileSize;
        mapHeight = tileHeight * tileSize;

        Main.player.width = tileSize;
        Main.player.height = (int) (tileSize * 1.5);

        for(PlayerPrefab player : playerList) {
            player.width = tileSize;
            player.height = (int) (tileSize * 1.5);
        }

        for(Building building : buildingList) {
            building.Scale();
        }
    }
}
