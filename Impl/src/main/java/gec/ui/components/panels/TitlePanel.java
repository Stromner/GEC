package gec.ui.components.panels;

import gec.core.ConsoleEnum;
import gec.core.events.ConsoleSelectedEvent;
import gec.ui.actions.KeyAction;
import gec.ui.components.elements.GECPanel;
import gec.ui.components.elements.MenuPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TitlePanel extends GECPanel {
    private static final Logger log = LoggerFactory.getLogger(TitlePanel.class);
    private Font defaultFontSize = new Font("Serif", Font.BOLD, 60);
    private Font selectedFontSize = new Font("Serif", Font.BOLD, 100);
    @Autowired
    MenuPanel menu;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostConstruct
    private void init() {
        List<String> enumList = Stream.of(ConsoleEnum.values())
                .map(ConsoleEnum::getConsoleName)
                .collect(Collectors.toList());

        menu.init(defaultFontSize, selectedFontSize, enumList, SwingConstants.CENTER);
        setKeyBindings();
        this.add(menu);
    }

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        String vkEnter = "ENTER";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), vkEnter);

        actionMap.put(vkEnter, new KeyAction(vkEnter, aVoid -> selectConsole()));
    }

    private Void selectConsole() {
        log.debug("Selected console '{}'", menu.getSelectedItem());
        eventPublisher.publishEvent(new ConsoleSelectedEvent(this, ConsoleEnum.get(menu.getSelectedItem())));
        return null;
    }
}
