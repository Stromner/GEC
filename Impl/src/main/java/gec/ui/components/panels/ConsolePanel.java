package gec.ui.components.panels;

import gec.core.ConsoleEnum;
import gec.core.FileHandler;
import gec.ui.components.elements.GECPanel;
import gec.ui.components.elements.MenuPanel;
import gec.ui.utils.GridBagUtil;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class ConsolePanel extends GECPanel {
    private Font defaultFontSize = new Font("Serif", Font.BOLD, 12);
    private Font selectedFontSize = new Font("Serif", Font.BOLD, 18);
    private ConsoleEnum selectedConsole;
    private String gameListPath;
    private List<String> gameList;

    public void init(ConsoleEnum selectedConsole) {
        // TODO Loading screen while we are initiating
        this.selectedConsole = selectedConsole;
        gameListPath = selectedConsole.getConsoleName() + ".txt";

        gameList = FileHandler.readLinesFromFile(gameListPath);
        createPanel();

    }

    private void createPanel() {
        MenuPanel menu = new MenuPanel();
        menu.init(defaultFontSize, selectedFontSize, gameList, SwingConstants.LEFT);
        this.add(menu);
        this.setBackground(Color.BLACK);
    }
}
