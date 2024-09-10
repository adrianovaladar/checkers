package checkers;

import java.util.AbstractMap.SimpleEntry;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker extends JButton {
    private static final String BLACK = "black";
    private static final String RED = "red";
    private static final String MAN = "man";
    private static final String KING = "king";

    SimpleEntry<Integer, Integer> position;
    transient Icon icon;
    transient Logger logger = Logger.getLogger(getClass().getName());

    public void kingRed() {
        this.setActionCommand(KING);
        this.setName(RED);
        Image img;

        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/red_king.png")));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to load red king image", ex);
        }
    }

    public void kingBlack() {
        this.setActionCommand(KING);
        this.setName(BLACK);
        Image img;

        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/black_king.png")));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to load black king image", ex);
        }
    }

    public void redMan() {
        this.setActionCommand(MAN);
        this.setName(RED);
        Image img;
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/red_an.png")));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to load red man image", ex);
        }
    }

    public void blackMan() {
        this.setActionCommand(MAN);
        this.setName(BLACK);
        Image img;
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/black_man.png")));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to load black man image", ex);
        }
    }

    public Checker(int i, int j) {
        position = new SimpleEntry<>(i, j);
        this.setBackground(new Color(158, 76, 16));

        if (i < 3) {
            blackMan();
        } else if (i > 4) {
            redMan();
        } else {
            this.setActionCommand("");
            this.setName("");
            this.setIcon(null);
        }
    }

    public Checker() {

    }

    public boolean isRed() {
        return this.getName().equals(RED);
    }

    public boolean isBlack() {
        return this.getName().equals(BLACK);
    }

    public boolean isMan() {
        return this.getActionCommand().equals(MAN);
    }

    public boolean isKing() {
        return this.getActionCommand().equals(KING);
    }

}
