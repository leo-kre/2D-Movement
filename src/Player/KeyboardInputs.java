package Player;

import Main.Main;
import UI.SettingsMenu;
import UI.UIManager;
import Main.Renderer;
import Main.GameHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT -> Movement.Dash(Main.player);
            case KeyEvent.VK_CONTROL -> Main.player.shouldWallRun = true;
            case KeyEvent.VK_W -> Main.player.up = true;
            case KeyEvent.VK_A -> Main.player.left = true;
            case KeyEvent.VK_S -> Main.player.down = true;
            case KeyEvent.VK_D -> Main.player.right = true;
            case KeyEvent.VK_F11 -> UIManager.isFullscreen = !UIManager.isFullscreen;
            case KeyEvent.VK_TAB -> UIManager.isTabOpen = false;
            case KeyEvent.VK_SPACE -> Movement.Jump(Main.player);
            case KeyEvent.VK_F5 -> {
                Renderer.ResetOffsets();
                Renderer.centerCamera = !Renderer.centerCamera;
                if(!Renderer.centerCamera) {
                    Renderer.ResetCamera();
                }
            }
            case KeyEvent.VK_G -> Renderer.ShakeCamera(1, 10);
            case KeyEvent.VK_R -> {
                GameHandler.InitGameScene();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL -> Main.player.shouldWallRun = false;
            case KeyEvent.VK_W -> Main.player.up = false;
            case KeyEvent.VK_A -> Main.player.left = false;
            case KeyEvent.VK_S -> Main.player.down = false;
            case KeyEvent.VK_D -> Main.player.right = false;
            case KeyEvent.VK_TAB -> UIManager.isTabOpen = true;
        }
    }
}
