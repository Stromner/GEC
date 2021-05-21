package gec.ui.components.panels;

import gec.core.events.MenuChangeEvent;
import gec.data.console.ConsoleHandler;
import gec.ui.components.elements.GECPanel;
import gec.ui.components.elements.MenuPanel;
import gec.ui.layouts.RelativeLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class ConsolePanel extends GECPanel {
    @Autowired
    ConsoleHandler consoleHandler;
    @Autowired
    MenuPanel menu;
    JLabel previewImage;
    private static float THIRTY_PCT = 30;
    private static float SEVENTY_PCT = 70;
    private Font defaultFontSize = new Font("Serif", Font.BOLD, 12);
    private Font selectedFontSize = new Font("Serif", Font.BOLD, 18);
    private List<String> gameList;

    public void init() {
        // TODO Loading screen while we are initiating
        gameList = consoleHandler.getGameList();

        this.setLayout(new GridBagLayout());
        createPanel();
    }

    @EventListener
    public void onMenuChangeEvent(MenuChangeEvent event) {
        if (this.isDisplayable()) {
            previewImage.setIcon(new ImageIcon(consoleHandler.getGamePreviewImage(gameList.get(event.getCurrentIndex()))));
        }
    }

    private void createPanel() {
        var layout = new RelativeLayout(RelativeLayout.X_AXIS);
        layout.setFill(true);
        layout.setAlignment(RelativeLayout.LEADING);
        this.setLayout(layout);

        createMenuPanel();
        this.add(new JSeparator(SwingConstants.VERTICAL));
        createPreviewPanel();
        // TODO Separator between these two? Boarder?
        // TODO Code for "menu" below preview image
    }

    private void createMenuPanel() {
        GECPanel menuPanel = new GECPanel();

        menu.init(defaultFontSize, selectedFontSize, gameList, SwingConstants.LEADING);
        menuPanel.add(menu);

        this.add(menuPanel, THIRTY_PCT);
    }

    private void createPreviewPanel() {
        GECPanel previewPanel = new GECPanel();
        previewPanel.setLayout(new GridLayout(1, 1));
        previewImage = new JLabel();
        previewImage.setIcon(new ImageIcon(consoleHandler.getGamePreviewImage(gameList.get(0))));
        previewPanel.add(previewImage);
        this.add(previewPanel, SEVENTY_PCT);
    }
}
