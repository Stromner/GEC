package gec.ui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class KeyAction extends AbstractAction {
    private final Consumer<Void> executeFunction;

    public KeyAction(String actionCommand, Consumer<Void> executeFunction) {
        this.executeFunction = executeFunction;
        putValue(ACTION_COMMAND_KEY, actionCommand);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvt) {
        executeFunction.accept(null);
    }
}
