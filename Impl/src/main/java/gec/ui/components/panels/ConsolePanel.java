package gec.ui.components.panels;

import gec.data.console.ConsoleHandler;
import gec.ui.components.elements.GECPanel;
import gec.ui.components.panels.partial.GamePanel;
import gec.ui.components.panels.partial.MenuPanel;
import gec.ui.layouts.RelativeLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class ConsolePanel extends GECPanel {
    private static final float THIRTY_PCT = 30;
    private static final float SEVENTY_PCT = 70;
    private final Font defaultFontSize = new Font("Serif", Font.BOLD, 12);
    private final Font selectedFontSize = new Font("Serif", Font.BOLD, 18);
    @Autowired
    private ConsoleHandler consoleHandler;
    @Autowired
    private MenuPanel menu;
    @Autowired
    private GamePanel gamePanel;
    private List<String> gameList;

    public void init() {
        gameList = consoleHandler.getGameList();

        createPanel();
    }

    private void createPanel() {
        var layout = new RelativeLayout(RelativeLayout.X_AXIS);
        layout.setFill(true);
        layout.setAlignment(RelativeLayout.LEADING);
        this.setLayout(layout);

        createMenuPanel();
        this.add(new JSeparator(SwingConstants.VERTICAL));
        createGamePanel();
    }

    private void createMenuPanel() {
        GECPanel menuPanel = new GECPanel();

        menu.init(defaultFontSize, selectedFontSize, gameList, SwingConstants.LEADING);
        menuPanel.add(menu);

        this.add(menuPanel, THIRTY_PCT);
    }

    private void createGamePanel() {
        gamePanel.init();
        this.add(gamePanel, SEVENTY_PCT);
    }
}
