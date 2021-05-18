package gec.ui.components.panels;

import gec.core.ConsoleEnum;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class ConsolePanel extends JPanel {
    private ConsoleEnum selectedConsole;

    public void init(ConsoleEnum selectedConsole) {
        this.selectedConsole = selectedConsole;
        this.setBackground(Color.BLACK);
    }
}
