package gec.ui.components.panels;

import gec.core.events.ConsoleSelectedEvent;
import gec.data.console.ConsoleHandler;
import gec.ui.components.elements.GECPanel;
import gec.ui.components.elements.LoadingGlassPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

@Component
public class ContentPanel extends GECPanel {
    @Autowired
    TitlePanel titlePanel;
    @Autowired
    ConsolePanel consolePanel;
    @Autowired
    ConsoleHandler consoleHandler;

    @PostConstruct
    private void init() {
        this.setLayout(new GridLayout(1, 1));
        this.add(titlePanel);
    }

    @EventListener
    public void onConsoleSelectedEvent(ConsoleSelectedEvent event) {
        LoadingGlassPane.getInstance().activate(this);

        CompletableFuture.supplyAsync(() -> {
            consoleHandler.selectConsole(event.getSelectedConsole());

            this.removeAll();
            this.add(consolePanel);
            consolePanel.init();
            this.repaint(); // Remove old
            this.revalidate(); // Add new

            LoadingGlassPane.getInstance().deactivate();
            return null;
        });
    }
}