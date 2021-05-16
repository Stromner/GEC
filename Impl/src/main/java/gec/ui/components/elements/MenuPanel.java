package gec.ui.components.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class MenuPanel extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(MenuPanel.class);
    private final List<MenuLabel> menuLabelList;
    private List<String> itemList;
    private int startIndex;
    private int endIndex;

    public MenuPanel() {
        menuLabelList = new ArrayList<>();
    }

    public void init(List<String> itemList) {
        this.itemList = itemList;
        if (itemList.size() == 0) {
            log.error("No supported consoles found!");
            throw new RuntimeException("No supported consoles found!");
        }

        startIndex = 0;
        endIndex = itemList.size() - 1;

        createPanel();
        setKeyBindings();
    }

    private void createPanel() {
        this.setBackground(Color.BLACK);
        for (String console : itemList) {
            MenuLabel menuLabel = new MenuLabel(console);
            menuLabelList.add(menuLabel);
            this.add(menuLabel);
        }
        menuLabelList.get(startIndex).toggleSelected();
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

    private void handleKeyPressed(Function<Integer, Integer> indexCalculator) {
        menuLabelList.get(startIndex).toggleSelected();
        startIndex = indexCalculator.apply(startIndex);
        endIndex = indexCalculator.apply(endIndex);
        menuLabelList.get(startIndex).toggleSelected();
    }

    private int decreaseIndex(int index) {
        return index-- <= 0 ? menuLabelList.size() - 1 : index--;
    }

    private int increaseIndex(int index) {
        return index++ >= menuLabelList.size() - 1 ? 0 : index++;
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
    }
}
