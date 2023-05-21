package UI;

import Main.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsMenu extends JPanel {
    public void Init() {
        this.setBackground(Color.blue);
        JButton exitGameButton = new JButton("EXIT");
        this.add(exitGameButton);
        exitGameButton.addActionListener(e -> System.exit(0));

        JButton toggleCenterCamera = new JButton("Center Camera");
        this.add(toggleCenterCamera);
        toggleCenterCamera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Renderer.centerCamera = !Renderer.centerCamera;
                if(Renderer.centerCamera) {
                    toggleCenterCamera.setText("Don't center camera");
                } else {
                    toggleCenterCamera.setText("Center camera");
                    Renderer.ResetCamera();
                }
                Renderer.ResetOffsets();
            }
        });

        JCheckBox box = new JCheckBox();
        this.add(box);
    }
}
