package gec.ui.components.panels;

import gec.core.ConsoleEnum;
import gec.ui.components.elements.MenuPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TitlePanel extends JPanel {
    private Font defaultFontSize = new Font("Serif", Font.BOLD, 60);
    private Font selectedFontSize = new Font("Serif", Font.BOLD, 100);
    @Autowired
    MenuPanel menu;

    @PostConstruct
    private void init() {
        List<String> enumList = Stream.of(ConsoleEnum.values())
                .map(ConsoleEnum::getConsoleName)
                .collect(Collectors.toList());

        menu.init(defaultFontSize, selectedFontSize, enumList, new GridLayout(0, 1));
        this.add(menu);

        this.setBackground(Color.BLACK);
    }
}
