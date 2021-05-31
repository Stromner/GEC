package gec.ui.components.elements;

import gec.ui.utils.ImageHelper;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;

@Component
public class LoadingGlassPane extends GECPanel implements KeyListener {
    private static LoadingGlassPane instance;
    private final Image originalImage;
    private Image animatedImage;

    public LoadingGlassPane() {
        super();

        setOpaque(false);

        Color base = UIManager.getColor("inactiveCaptionBorder");
        Color background = new Color(base.getRed(), base.getGreen(), base.getBlue(), 128);
        setBackground(background);

        URL url = getClass().getClassLoader().getResource("loadingAnimated.gif");
        originalImage = new ImageIcon(url).getImage();

        disableInput();
    }

    public static LoadingGlassPane getInstance() {
        if (instance == null) {
            instance = new LoadingGlassPane();
        }
        return instance;
    }

    public void activate(GECPanel parentPanel) {
        JRootPane root = SwingUtilities.getRootPane(parentPanel);
        root.setGlassPane(instance);

        setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        requestFocusInWindow();
    }

    public void deactivate() {
        setCursor(null);
        setVisible(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        e.consume();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        e.consume();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getSize().width, getSize().height);

        setRelativePreferredImageSizeToParent();
        paintInRelationToParent(g);
    }

    private void setRelativePreferredImageSizeToParent() {
        int height = this.getParent().getSize().height / 4;
        if (animatedImage == null || height != animatedImage.getHeight(this)) {
            animatedImage = ImageHelper.rescaleImage(originalImage, height, height); // On purpose, to keep ratio
        }
    }

    private void paintInRelationToParent(Graphics g) {
        int parentWidth = this.getParent().getSize().width;
        int parentHeight = this.getParent().getSize().height;
        int x = (parentWidth - animatedImage.getWidth(this)) / 2;
        int y = (parentHeight - animatedImage.getHeight(this)) / 2;
        g.drawImage(animatedImage, x, y, this);
    }


    private void disableInput() {
        addMouseListener(new MouseAdapter() {
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
    }
}
