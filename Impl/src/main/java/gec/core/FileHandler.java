package gec.core;

import gec.ui.components.panels.ConsolePanel;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
    public static List<String> readLinesFromFile(String filePath) {
        URL url = ConsolePanel.class.getClassLoader().getResource(filePath);
        List<String> list = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File(url.toURI()));
            while (s.hasNextLine()) {
                String row = s.nextLine();
                // Ignore comment rows
                if (row.charAt(0) == '#') {
                    continue;
                }
                list.add(row);
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Nice error to UI
        }

        return list;
    }
}
