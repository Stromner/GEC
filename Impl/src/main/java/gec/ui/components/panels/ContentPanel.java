package gec.ui.components.panels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
public class ContentPanel extends JPanel {
    @Autowired
    TitlePanel titlePanel;

    @PostConstruct
    private void init() {
        this.setBackground(Color.BLACK);
        this.add(titlePanel);
    }
}