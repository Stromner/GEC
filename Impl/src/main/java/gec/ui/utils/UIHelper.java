package gec.ui.utils;

import gec.ui.components.elements.GECPanel;
import gec.ui.components.elements.LoadingGlassPane;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class UIHelper {
    public static void displayLoadingForMethodCall(Runnable runnable, GECPanel parentPanel) {
        LoadingGlassPane.getInstance().activate(parentPanel);
        CompletableFuture.runAsync(() -> {
            runnable.run();
            LoadingGlassPane.getInstance().deactivate();
        });
    }

    public static <T> void displayLoadingForMethodCall(Consumer<T> consumer, T object, GECPanel parentPanel) {
        LoadingGlassPane.getInstance().activate(parentPanel);
        CompletableFuture.runAsync(() -> {
            consumer.accept(object);
            LoadingGlassPane.getInstance().deactivate();
        });
    }
}
