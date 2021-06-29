package gec.ui.components.panels;

import gec.core.events.MenuChangeEvent;
import gec.data.console.ConsoleHandler;
import gec.ui.components.elements.GECPanel;
import gec.ui.components.elements.LoadingGlassPane;
import gec.ui.components.panels.partial.MenuPanel;
import gec.ui.layouts.RelativeLayout;
import gec.ui.utils.ImageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    private BufferedImage previewImage;
    private JLabel previewImageLabel;
    private List<String> gameList;

    public void init() {
        gameList = consoleHandler.getGameList();

        createPanel();
    }

    @EventListener
    public void onMenuChangeEvent(MenuChangeEvent event) {
        if (this.isDisplayable()) {
            LoadingGlassPane.getInstance().activate(this);

            CompletableFuture.supplyAsync(() -> {
                previewImage = consoleHandler.getGamePreviewImage(gameList.get(event.getCurrentIndex()));
                previewImageLabel.setIcon(new ImageIcon(ImageHelper.rescaleImage(previewImage, previewImageLabel.getWidth(), previewImageLabel.getHeight())));
                LoadingGlassPane.getInstance().deactivate();
                return null;
            });
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
        previewImageLabel = new JLabel();
        previewImage = consoleHandler.getGamePreviewImage(gameList.get(0));
        previewImageLabel.setIcon(new ImageIcon(previewImage));

        previewPanel.add(previewImageLabel);
        previewPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                previewImageLabel.setIcon(new ImageIcon(ImageHelper.rescaleImage(
                        previewImage,
                        previewImageLabel.getWidth(),
                        previewImageLabel.getHeight())));
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        this.add(previewPanel, SEVENTY_PCT);
    }

}
