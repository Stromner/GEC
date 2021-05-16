package gec.ui;

import gec.ui.components.panels.ContentPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

@Component
@ConditionalOnProperty(name = "program.live.boot")
public class ApplicationWindow {
    private JFrame frame;
    @Autowired
    private ContentPanel contentPanel;

    @PostConstruct
    public void init() {
        createFrame();

        frame.setVisible(true);
        frame.setContentPane(contentPanel);
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

            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
