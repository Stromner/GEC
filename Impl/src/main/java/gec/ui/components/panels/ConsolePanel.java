package gec.ui.components.panels;

import gec.data.console.ConsoleHandler;
import gec.ui.components.elements.GECPanel;
import gec.ui.components.elements.MenuPanel;
import gec.ui.layouts.RelativeLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class ConsolePanel extends GECPanel {
    @Autowired
    ConsoleHandler consoleHandler;
    private static float THIRTY_PCT = 3;
    private static float SEVENTY_PCT = 7;
    private Font defaultFontSize = new Font("Serif", Font.BOLD, 12);
    private Font selectedFontSize = new Font("Serif", Font.BOLD, 18);
    private List<String> gameList;

    public void init() {
        // TODO Loading screen while we are initiating
        gameList = consoleHandler.getGameList();

        this.setLayout(new GridBagLayout());
        createPanel();
    }

    private void createPanel() {
        var layout = new RelativeLayout(RelativeLayout.X_AXIS);
        layout.setFill(true);
        layout.setAlignment(RelativeLayout.LEADING);
        this.setLayout(layout);

        createMenuPanel();
        this.add(new JSeparator(SwingConstants.VERTICAL));
        // TODO Code for preview image
        // TODO Separator between these two? Boarder?
        // TODO Code for "menu" below preview image
    }

    private void createMenuPanel() {
        GECPanel menuPanel = new GECPanel();

        MenuPanel menu = new MenuPanel();
        menu.init(defaultFontSize, selectedFontSize, gameList, SwingConstants.LEADING);
        menuPanel.add(menu);

        this.add(menuPanel, THIRTY_PCT);
    }
}
