package gec.ui.components.panels;

import gec.ui.components.elements.MenuPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class TitlePanel extends JPanel {
    private Font defaultFontSize = new Font("Serif", Font.BOLD, 60);
    private Font selectedFontSize = new Font("Serif", Font.BOLD, 100);
    @Value("#{'${supported.games}'.split(',')}")
    private List<String> consoleList;
    @Autowired
    MenuPanel menu;

    @PostConstruct
    private void init() {
        menu.init(defaultFontSize, selectedFontSize, consoleList, new GridLayout(0, 1));
        this.add(menu);

        this.setBackground(Color.BLACK);
    }
}
