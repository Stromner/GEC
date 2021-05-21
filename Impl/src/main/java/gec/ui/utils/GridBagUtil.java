package gec.ui.utils;

import javax.swing.*;
import java.awt.*;

public class GridBagUtil {
    public static void addComponent(JComponent parentContainer, JComponent component, int gridX, int gridY) {
        addComponentWithConstraints(parentContainer, component, gridX, gridY, new GridBagConstraints());
    }

    public static void addComponentWithConstraints
            (JComponent parentContainer, JComponent component, int gridX, int gridY, GridBagConstraints additionalConstraints) {
        additionalConstraints.gridx = gridX;
        additionalConstraints.gridy = gridY;

        parentContainer.add(component, additionalConstraints);
    }
}
