package gec.core.events;

import org.springframework.context.ApplicationEvent;

public class MenuChangeEvent extends ApplicationEvent {
    private final Integer currentIndex;

    public MenuChangeEvent(Object source, Integer currentIndex) {
        super(source);
        this.currentIndex = currentIndex;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }
}
