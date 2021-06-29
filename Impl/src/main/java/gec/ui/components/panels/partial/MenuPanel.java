package gec.ui.components.panels.partial;

import gec.core.events.MenuChangeEvent;
import gec.ui.actions.KeyAction;
import gec.ui.components.elements.GECPanel;
import gec.ui.components.elements.MenuLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MenuPanel extends GECPanel {
    private static final Logger log = LoggerFactory.getLogger(MenuPanel.class);
    private final List<MenuLabel> menuLabelList;
    private List<String> itemList;
    private int selectedIndex;
    private int currentPanelHeight = 0;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public MenuPanel() {
        super();
        menuLabelList = new ArrayList<>();
    }

    public void init(Font defaultFontSize, Font selectedFontSize, List<String> itemList, int menuAlignment) {
        this.itemList = itemList;
        if (itemList.size() == 0) {
            log.error("List of items is empty!");
            throw new RuntimeException("List of items is empty!"); // TODO Graceful error to UI?
        }

        selectedIndex = 0;

        this.setLayout(new GridLayout(0, 1));
        createPanel(defaultFontSize, selectedFontSize, menuAlignment);
        setKeyBindings();

        var us = this;
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                currentPanelHeight = SwingUtilities.getRootPane(us).getHeight();
                reorderMenuItems();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                currentPanelHeight = SwingUtilities.getRootPane(us).getHeight();
                reorderMenuItems();
            }
        });
    }

    public String getSelectedItem() {
        return itemList.get(selectedIndex);
    }

    private void createPanel(Font defaultFontSize, Font selectedFontSize, int menuAlignment) {
        for (String console : itemList) {
            MenuLabel menuLabel = new MenuLabel(console, defaultFontSize, selectedFontSize);
            menuLabel.setHorizontalAlignment(menuAlignment);
            menuLabelList.add(menuLabel);
        }
        reorderMenuItems();
        menuLabelList.get(selectedIndex).toggleSelected();
    }

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        String vkUp = "VK_UP";
        String vkDown = "VK_DOWN";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), vkUp);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), vkDown);

        actionMap.put(vkUp, new KeyAction(vkUp, aVoid -> switchMenuItem(this::decreaseIndex)));
        actionMap.put(vkDown, new KeyAction(vkDown, aVoid -> switchMenuItem(this::increaseIndex)));
    }

    private void reorderMenuItems() {
        removeAll();

        int fontHeight = menuLabelList.get(selectedIndex).getDefaultFontSize().getSize();
        int nrOfElements = Math.min(currentPanelHeight / (fontHeight * 2), menuLabelList.size());
        int backHalf = nrOfElements / 2;
        int iteratorIndex = selectedIndex;

        // Navigate to start
        for (int i = 0; i < backHalf; i++) {
            iteratorIndex = decreaseIndex(iteratorIndex);
        }

        // Add entries
        for (int i = 0; i < nrOfElements; i++) {
            add(menuLabelList.get(iteratorIndex));
            iteratorIndex = increaseIndex(iteratorIndex);
        }
    }

    private void switchMenuItem(Function<Integer, Integer> indexCalculator) {
        handleKeyPressed(indexCalculator);
        reorderMenuItems();
        eventPublisher.publishEvent(new MenuChangeEvent(this, selectedIndex));
    }

    private void handleKeyPressed(Function<Integer, Integer> indexCalculator) {
        menuLabelList.get(selectedIndex).toggleSelected();
        selectedIndex = indexCalculator.apply(selectedIndex);
        menuLabelList.get(selectedIndex).toggleSelected();
    }

    private int decreaseIndex(int index) {
        return index-- <= 0 ? menuLabelList.size() - 1 : index--;
    }

    private int increaseIndex(int index) {
        return index++ >= menuLabelList.size() - 1 ? 0 : index++;
    }
}
