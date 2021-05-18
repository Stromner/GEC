package gec.ui.components.elements;

import gec.core.ConsoleEnum;
import gec.core.events.ConsoleSelectedEvent;
import gec.ui.actions.KeyAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
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
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public MenuPanel() {
        menuLabelList = new ArrayList<>();
    }

    public void init(Font defaultFontSize, Font selectedFontSize, List<String> itemList, int menuAlignment) {
        this.itemList = itemList;
        if (itemList.size() == 0) {
            log.error("List of items is empty!");
            throw new RuntimeException("List of items is empty!"); // TODO Graceful error to UI?
        }

        startIndex = 0;
        endIndex = itemList.size() - 1;

        createPanel(defaultFontSize, selectedFontSize, menuAlignment);
        setKeyBindings();
        this.setLayout(new GridLayout(0, 1));
    }

    private void createPanel(Font defaultFontSize, Font selectedFontSize, int menuAlignment) {
        this.setBackground(Color.BLACK);
        for (String console : itemList) {
            MenuLabel menuLabel = new MenuLabel(console, defaultFontSize, selectedFontSize);
            menuLabel.setHorizontalAlignment(menuAlignment);
            menuLabelList.add(menuLabel);
        }
        reorderMenuItems();
        menuLabelList.get(startIndex).toggleSelected();
    }

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        String vkUp = "VK_UP";
        String vkDown = "VK_DOWN";
        String vkEnter = "ENTER";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), vkUp);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), vkDown);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), vkEnter);

        actionMap.put(vkUp, new KeyAction(vkUp, aVoid -> switchMenuItem(this::decreaseIndex)));
        actionMap.put(vkDown, new KeyAction(vkDown, aVoid -> switchMenuItem(this::increaseIndex)));
        actionMap.put(vkEnter, new KeyAction(vkEnter, aVoid -> selectConsole()));
    }

    private void reorderMenuItems() {
        removeAll();
        add(menuLabelList.get(endIndex));
        int index = endIndex;
        index = increaseIndex(index);
        while (index != endIndex) {
            add(menuLabelList.get(index));
            index = increaseIndex(index);
        }
    }

    private Void switchMenuItem(Function<Integer, Integer> indexCalculator) {
        handleKeyPressed(indexCalculator);
        reorderMenuItems();
        return null;
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

    private Void selectConsole() {
        log.debug("Selected console '{}'", itemList.get(startIndex));
        eventPublisher.publishEvent(new ConsoleSelectedEvent(this, ConsoleEnum.get(itemList.get(startIndex))));
        return null;
    }
}
