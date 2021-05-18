package gec.core.events;

import gec.core.ConsoleEnum;
import org.springframework.context.ApplicationEvent;

public class ConsoleSelectedEvent extends ApplicationEvent {
    private ConsoleEnum selectedConsole;

    public ConsoleSelectedEvent(Object source, ConsoleEnum selectedConsole) {
        super(source);
        this.selectedConsole = selectedConsole;
    }

    public ConsoleEnum getSelectedConsole() {
        return selectedConsole;
    }
}