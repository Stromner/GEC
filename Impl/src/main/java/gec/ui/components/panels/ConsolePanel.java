package gec.ui.components.panels;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class ConsolePanel extends JPanel {
    private String selectedConsole;

    public void init(String selectedConsole) {
        this.selectedConsole = selectedConsole;
        this.setBackground(Color.BLACK);
    }
}
