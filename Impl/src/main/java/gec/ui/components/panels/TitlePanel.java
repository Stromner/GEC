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
    @Value("#{'${supported.games}'.split(',')}")
    private List<String> consoleList;
    @Autowired
    MenuPanel menu;

    @PostConstruct
    private void init() {
        menu.init(consoleList);
        this.add(menu);

        this.setBackground(Color.BLACK);
    }
}
