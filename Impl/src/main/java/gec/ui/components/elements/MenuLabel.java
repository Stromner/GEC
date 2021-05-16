package gec.ui.components.elements;

import javax.swing.*;
import java.awt.*;

public class MenuLabel extends JLabel {
    public static final Font DEFAULT_FONT_SIZE = new Font("Serif", Font.BOLD, 70);
    public static final Font SELECTED_FONT_SIZE = new Font("Serif", Font.BOLD, 100);
    private boolean isSelected;

    public MenuLabel(String data) {
        super(data);
        isSelected = false;
        this.setForeground(Color.WHITE);
        this.setFont(DEFAULT_FONT_SIZE);
    }

    public void toggleSelected() {
        isSelected = !isSelected;

        this.setFont(isSelected ? SELECTED_FONT_SIZE : DEFAULT_FONT_SIZE);
    }
}
