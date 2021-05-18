package gec.ui.components.elements;

import javax.swing.*;
import java.awt.*;

public class MenuLabel extends JLabel {
    private Font defaultFontSize;
    private Font selectedFontSize;
    private boolean isSelected;

    public MenuLabel(String data, Font defaultFontSize, Font selectedFontSize) {
        super(data);
        this.defaultFontSize = defaultFontSize;
        this.selectedFontSize = selectedFontSize;

        isSelected = false;
        this.setForeground(Color.WHITE);
        this.setFont(defaultFontSize);
        this.setAlignmentX(0.5f);
        this.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void toggleSelected() {
        isSelected = !isSelected;

        this.setFont(isSelected ? selectedFontSize : defaultFontSize);
    }
}
