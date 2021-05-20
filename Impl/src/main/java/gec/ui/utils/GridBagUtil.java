package gec.ui.utils;

import org.springframework.lang.Nullable;

import javax.swing.*;
import java.awt.*;

public class GridBagUtil {
    public static void addComponent(JComponent parentContainer, JComponent component, int gridX, int gridY) {
        addComponentWithConstraints(parentContainer, component, gridX, gridY, null);
    }

    public static void addComponentWithConstraints
            (JComponent parentContainer, JComponent component, int gridX, int gridY, @Nullable GridBagConstraints additionalConstraints) {
        if (additionalConstraints == null) {
            additionalConstraints = new GridBagConstraints();
        }
        additionalConstraints.gridx = gridX;
        additionalConstraints.gridy = gridY;

        parentContainer.add(component, additionalConstraints);
    }
}
