package gec.ui.components.panels;

import gec.core.ConsoleEnum;
import gec.core.FileHandler;
import gec.ui.components.elements.MenuPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class ConsolePanel extends JPanel {
    @Autowired
    ResourceLoader resourceLoader;
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
        menu.init(defaultFontSize, selectedFontSize, gameList, new GridLayout(0, 1));
        this.add(menu);
        this.setBackground(Color.BLACK);
    }
}
