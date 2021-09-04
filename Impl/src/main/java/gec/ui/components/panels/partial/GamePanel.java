package gec.ui.components.panels.partial;

import gec.core.ConsoleEnum;
import gec.core.events.MenuChangeEvent;
import gec.data.GameMetaData;
import gec.data.console.ConsoleHandler;
import gec.data.file.FileHandler;
import gec.data.rom.crawler.RomManager;
import gec.ui.components.elements.GECPanel;
import gec.ui.components.elements.LoadingGlassPane;
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
public class GamePanel extends GECPanel {
    private static final float TWENTY_PCT = 20;
    private static final float EIGHTY_PCT = 80;
    @Autowired
    private ConsoleHandler consoleHandler;
    @Autowired
    private FileHandler fileHandler;
    @Autowired
    private RomManager romManager;
    private BufferedImage previewImage;
    private JLabel previewImageLabel;
    private List<String> gameList;
    private JButton romButton;

    public void init() {
        gameList = consoleHandler.getGameList();

        createPanel();
    }

    @EventListener
    public void onMenuChangeEvent(MenuChangeEvent event) {
        if (this.isDisplayable()) { // TODO See if we can remove this if statement somehow
            LoadingGlassPane.getInstance().activate(this);

            CompletableFuture.supplyAsync(() -> checkForRomOnDisk(event.getCurrentIndex()));

            CompletableFuture.supplyAsync(() -> {
                previewImage = consoleHandler.getGamePreviewImage(gameList.get(event.getCurrentIndex()));
                previewImageLabel.setIcon(new ImageIcon(
                        ImageHelper.rescaleImage(previewImage, previewImageLabel.getWidth(),
                                previewImageLabel.getHeight())));
                LoadingGlassPane.getInstance().deactivate();
                return null;
            });
        }
    }

    private void createPanel() {
        var layout = new RelativeLayout(RelativeLayout.Y_AXIS);
        layout.setFill(true);
        layout.setAlignment(RelativeLayout.LEADING);
        this.setLayout(layout);

        createPreviewPanel();
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        createSubMenu();
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

        this.add(previewPanel, EIGHTY_PCT);
    }

    private void createSubMenu() {
        GECPanel subMenu = new GECPanel();
        romButton = new JButton(); // TODO Probably want this as a custom element as it has to fire some code
        subMenu.add(romButton);
        this.add(subMenu, TWENTY_PCT);
        checkForRomOnDisk(0);
    }

    private Void checkForRomOnDisk(Integer itemInList) {
        ConsoleEnum console = ConsoleEnum.get(consoleHandler.getSelectedConsole());
        GameMetaData game = new GameMetaData(gameList.get(itemInList), console);
        if (fileHandler.romExists(game)) {
            romButton.setText("PLAY");
        } else {
            romButton.setText("DOWNLOAD");
        }

        return null;
    }
}
