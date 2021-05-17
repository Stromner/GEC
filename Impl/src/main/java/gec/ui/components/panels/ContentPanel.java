package gec.ui.components.panels;

import gec.core.events.ConsoleSelectedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
public class ContentPanel extends JPanel {
    @Autowired
    TitlePanel titlePanel;
    @Autowired
    ConsolePanel consolePanel;

    @PostConstruct
    private void init() {
        this.setBackground(Color.BLACK);
        this.add(titlePanel);
    }

    @EventListener
    public void onConsoleSelectedEvent(ConsoleSelectedEvent event) {
        this.removeAll();
        this.add(consolePanel);
        consolePanel.init(event.getSelectedConsole());
        this.repaint(); // Remove old
        this.revalidate(); // Add new
    }
}