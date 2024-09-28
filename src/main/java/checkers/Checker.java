package checkers;

import java.util.AbstractMap.SimpleEntry;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker extends JButton {
    public enum PieceType {MAN, KING, NONE}

    public enum PieceColour {RED, BLACK, NONE}
    SimpleEntry<Integer, Integer> position;
    transient Icon icon;
    transient Logger logger = Logger.getLogger(getClass().getName());
    private static final int BLACK_SETUP_END_ROW = 3;
    private static final int RED_SETUP_START_ROW = 4;

    private void setCheckerAttributes(PieceType type, PieceColour colour, String imagePath) {
        this.setActionCommand(type.name());
        this.setName(colour.name());
        try {
            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource(imagePath)));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex, () -> "Failed to load image: " + imagePath);
        }
    }

    public void setKing(PieceColour colour) {
        String imagePath;
        switch (colour) {
            case PieceColour.RED -> imagePath = "/red_king.png";
            case PieceColour.BLACK -> imagePath = "/black_king.png";
            default -> imagePath = "/none";
        }
        setCheckerAttributes(PieceType.KING, colour, imagePath);
    }

    public void redMan() {
        setCheckerAttributes(PieceType.MAN, PieceColour.RED, "/red_man.png");
    }

    public void blackMan() {
        setCheckerAttributes(PieceType.MAN, PieceColour.BLACK, "/black_man.png");
    }

    public Checker(int i, int j) {
        position = new SimpleEntry<>(i, j);
        if (i < BLACK_SETUP_END_ROW) {
            blackMan();
        } else if (i > RED_SETUP_START_ROW) {
            redMan();
        } else {
            this.setActionCommand(PieceType.NONE.name());
            this.setName(PieceColour.NONE.name());
            this.setIcon(null);
        }
    }

    public Checker() {

    }

    public boolean isRed() {
        return PieceColour.RED.name().equals(this.getName());
    }

    public boolean isBlack() {
        return PieceColour.BLACK.name().equals(this.getName());
    }

    public boolean isMan() {
        return PieceType.MAN.name().equals(this.getActionCommand());
    }

    public boolean isKing() {
        return PieceType.KING.name().equals(this.getActionCommand());
    }
}
