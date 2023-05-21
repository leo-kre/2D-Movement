package Main;

import GameScene.Building;
import Player.PlayerPrefab;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Renderer extends JPanel{

    public static boolean centerCamera = false;
    private static float cameraShakeTime;
    private static float cameraShakeAmount;

    private static float offsetX;
    private static float offsetY;

    private static float effectX;
    private static float effectY;

    private static float centerX;
    private static float centerY;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        g2d.setColor(Color.yellow);
        g2d.fillRect((int) (0 + effectX + centerX + offsetX), (int) (0 + effectY + centerY + offsetY), GameHandler.mapWidth, GameHandler.mapHeight);

        ArrayList<Building> buildingList = GameHandler.buildingList;
        for(Building building : buildingList) {
            g2d.setColor(building.color);
            g2d.fillRect((int) (building.rect.x + effectX + centerX + offsetX), (int) (building.rect.y + effectY + centerY + offsetY), building.rect.width, building.rect.height);
        }

        ArrayList<PlayerPrefab> playerList = GameHandler.playerList;
        for (PlayerPrefab player : playerList) {
            if(Objects.equals(player.name, Main.player.name)) return;
            g2d.setColor(player.color);
            g2d.fillRect((int) (player.x + effectX + centerX + offsetX), (int) (player.y + effectY + centerY + offsetY), player.width, player.height);
        }

        g2d.setColor(Main.player.color);
        g2d.fillRect((int) (Main.player.x + effectX + centerX + offsetX), (int) (Main.player.y + effectY + centerY + offsetY), Main.player.width, Main.player.height);
        g2d.setColor(Color.MAGENTA);
        g2d.drawRect((int) (Main.player.x + effectX + centerX + offsetX), (int) (Main.player.y + effectY + centerY + offsetY), Main.player.width, Main.player.height);
    }

    public static void ResetCamera() {
        effectX = 0;
        effectY = 0;
    }

    public static void ShakeCamera(float _timeInSeconds, float _amount) {
        cameraShakeTime = _timeInSeconds * 1000 * 1000;
        cameraShakeAmount = _amount;
    }

    public static void CenterGamePanel(JFrame _frame, boolean _vertically, boolean _horizontally) {

        Dimension fSize = _frame.getSize();
        Dimension gSize = new Dimension(GameHandler.mapWidth, GameHandler.mapHeight);

        if(_vertically) {
            centerY = (fSize.height - gSize.height) / 2;
        }

        if(_horizontally) {
            centerX = (fSize.width - gSize.width) / 2;
        }
    }

    public static void UpdateCameraEffects() {
            cameraShakeTime -= 1000 / Main.updatesPerSeconds;
            if(cameraShakeTime <= 0) {
                cameraShakeTime = 0;
                cameraShakeAmount = 0;
                effectX = 0;
                effectY = 0;
            } else if(!centerCamera){
                effectX = (float) (Math.random() * cameraShakeAmount);
                effectY = (float) (Math.random() * cameraShakeAmount);
            }
    }

    public static void CenterCamera(PlayerPrefab _player, JFrame _frame, boolean _vertically, boolean _horizontally) {
        int cX = _frame.getWidth() / 2;
        int cY = _frame.getHeight() / 2;

        if(_horizontally) {
            offsetX = cX - _player.x - _player.width;
            CenterGamePanel(_frame, true, false);
        }

        if(_vertically) {
            CenterGamePanel(_frame, false, true);
            offsetY = cY - _player.y - _player.height;
        }
    }

    public static void ResetOffsets() {
        offsetX = 0;
        offsetY = 0;

        centerX = 0;
        centerY = 0;

        effectX = 0;
        effectY = 0;
    }
}
