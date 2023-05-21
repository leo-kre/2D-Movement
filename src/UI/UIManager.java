package UI;

import Main.Main;
import Main.GameHandler;

import javax.swing.*;
import java.awt.*;

public class UIManager {

    //fullscreen
    public static boolean isFullscreen = true;
    private static boolean lastFullScreenState = false;
    //tab
    public static boolean isTabOpen = true;

    private static JFrame frame;

    private static JPanel tabPanel;

    public static void Init(JFrame _frame, JPanel _tabPanel) {
        frame = _frame;
        tabPanel = _tabPanel;
    }

    public static void Update() {
        SetFullscreen(isFullscreen);
        OpenTab(isTabOpen, tabPanel);
    }

    public static void OpenTab(boolean _state, JPanel _panel) {
        _panel.setVisible(_state);
    }
    public static void SetFullscreen(boolean _state) {
        if(_state && !lastFullScreenState) {
            frame.dispose();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            GameHandler.tileSize = 50;
            GameHandler.UpdateViewport();
            frame.setUndecorated(true);
            frame.setBackground(Color.black);
            frame.setVisible(true);
            lastFullScreenState = true;
        } else if(!_state && lastFullScreenState){
            frame.dispose();
            frame.setUndecorated(false);
            frame.setSize(Main.frameSize * 16, Main.frameSize * 9);
            frame.setExtendedState(JFrame.NORMAL);
            GameHandler.tileSize = 40;
            GameHandler.UpdateViewport();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            lastFullScreenState = false;
        }
    }
}
