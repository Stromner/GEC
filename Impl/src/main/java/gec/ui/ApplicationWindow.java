package gec.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
@ConditionalOnProperty(name = "program.live.boot")
public class ApplicationWindow {
    private JFrame frame;
    @Value("#{'${supported.games}'.split(',')}")
    private List<String> consoleList;

    @PostConstruct
    public void init() {
        createFrame();

        frame.setVisible(true);
    }
    private void createFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        configureFrame();
        frame.requestFocus();
    }

    private void configureFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            frame.getContentPane().setBackground(Color.BLACK);

            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
