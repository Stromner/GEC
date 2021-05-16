package gec.ui.components.panels;

import gec.ui.components.elements.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// TODO Move most of the code to a TitleScreen pane instead
@Component
public class ContentPanel extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(ContentPanel.class);
    private final List<MenuItem> menuItemList;
    private int startIndex;
    private int endIndex;
    @Value("#{'${supported.games}'.split(',')}")
    private List<String> consoleList;

    private ContentPanel() {
        menuItemList = new ArrayList<>();
    }

    @PostConstruct
    private void init() {
        if (consoleList.size() == 0) {
            log.error("No supported consoles found!");
            throw new RuntimeException("No supported consoles found!");
        }

        startIndex = 0;
        endIndex = consoleList.size() - 1;

        createPanel();
        setKeyBindings();
    }

    private void createPanel() {
        this.setBackground(Color.BLACK);
        for (String console : consoleList) {
            MenuItem menuItem = new MenuItem(console);
            menuItemList.add(menuItem);
            this.add(menuItem);
        }
        menuItemList.get(startIndex).toggleSelected();
    }

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        String vkUp = "VK_UP";
        String vkDown = "VK_DOWN";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), vkUp);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), vkDown);

        actionMap.put(vkUp, new KeyAction(vkUp, this::decreaseIndex));
        actionMap.put(vkDown, new KeyAction(vkDown, this::increaseIndex));
    }

    private int decreaseIndex(int index) {
        return index-- <= 0 ? menuItemList.size() - 1 : index--;
    }

    private int increaseIndex(int index) {
        return index++ >= menuItemList.size() - 1 ? 0 : index++;
    }

    private class KeyAction extends AbstractAction {
        private final Function<Integer, Integer> indexCalculator;

        public KeyAction(String actionCommand, Function<Integer, Integer> indexCalculator) {
            this.indexCalculator = indexCalculator;
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvt) {
            handleKeyPressed(indexCalculator);
        }

        private void handleKeyPressed(Function<Integer, Integer> indexCalculator) {
            menuItemList.get(startIndex).toggleSelected();
            startIndex = indexCalculator.apply(startIndex);
            endIndex = indexCalculator.apply(endIndex);
            menuItemList.get(startIndex).toggleSelected();
        }
    }
}