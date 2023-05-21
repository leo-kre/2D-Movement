package Main;

import Player.KeyboardInputs;
import UI.UIManager;
import Player.Movement;
import Player.PlayerPrefab;
import UI.SettingsMenu;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    //frame
    public static JFrame frame =  new JFrame();
    public static JPanel gamePanel;
    public static String frameTitle = "Multiplayer";

    public static KeyboardInputs keyboardInputs = new KeyboardInputs();

    //client Player.Player
    public static PlayerPrefab player;

    // Settings
    public static SettingsMenu settingsMenu;

    //frame Settings
    public static int updatesPerSeconds = 60;
    public static int frameSize = 100;

    public static void main(String[] args) throws IOException {
        gamePanel = new Renderer();

        settingsMenu = new SettingsMenu();
        settingsMenu.Init();

        InitFrame(frame, frameTitle);
        InitListeners(frame);
        UIManager.Init(frame, gamePanel);
        GameHandler.InitGameScene();
        StartGameTimer(updatesPerSeconds);

        GameHandler.InitPlayer(true, "Firewiesel", 100, 100, Color.blue);
    }

    public static void GameLoop() {
        Movement.Calculate();

        UIManager.Update();

        if(Renderer.centerCamera) {
            Renderer.CenterCamera(player, frame, false, true);
        } else {
            Renderer.CenterGamePanel(frame, true, true);
        }

        Renderer.UpdateCameraEffects();

        gamePanel.repaint();
    }

    public static void InitFrame(JFrame _frame, String _title) {
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(frameSize * 16, frameSize * 9);
        _frame.setTitle(_title);
        _frame.setLocationRelativeTo(null);
        _frame.setFocusTraversalKeysEnabled(false);
        _frame.setResizable(false);

        _frame.setBackground(Color.black);

        _frame.setLayout(new CardLayout());
        _frame.add(settingsMenu);
        _frame.add(gamePanel);
        _frame.setVisible(true);
    }

    public static void InitListeners(JFrame _frame) {
        _frame.addKeyListener(new KeyboardInputs());
        _frame.setFocusable(true);

        settingsMenu.addKeyListener(new KeyboardInputs());
        settingsMenu.setFocusable(true);

        gamePanel.addKeyListener(new KeyboardInputs());
        gamePanel.setFocusable(true);
    }

    public static void StartGameTimer(int _fps) {

        Runnable helloRunnable = new Runnable() {
            public void run() {
                GameLoop();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 1000/_fps, TimeUnit.MICROSECONDS);
    }
}
