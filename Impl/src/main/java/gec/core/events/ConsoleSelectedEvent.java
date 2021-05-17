package gec.core.events;

import org.springframework.context.ApplicationEvent;

public class ConsoleSelectedEvent extends ApplicationEvent {
    private String selectedConsole;

    public ConsoleSelectedEvent(Object source, String selectedConsole) {
        super(source);
        this.selectedConsole = selectedConsole;
    }

    public String getSelectedConsole() {
        return selectedConsole;
    }
}