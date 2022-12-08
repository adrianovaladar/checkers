import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class Checker extends JButton {

    Pair<Integer, Integer> position;

    Icon icon;

    public void kingRed() {
        this.setActionCommand("king");
        this.setName("red");
        Image img;

        try {
            img = ImageIO.read(getClass().getResource("resources/red_king.png"));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void kingBlack() {
        this.setActionCommand("king");
        this.setName("black");
        Image img;

        try {
            img = ImageIO.read(getClass().getResource("resources/black_king.png"));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void redMan() {
        this.setActionCommand("man");
        this.setName("red");
        Image img;
        try {
            img = ImageIO.read(getClass().getResource("resources/red_man.png"));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void blackMan() {
        this.setActionCommand("man");
        this.setName("black");
        Image img;
        try {
            img = ImageIO.read(getClass().getResource("resources/black_man.png"));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public Checker(int i, int j) {
        position = new Pair<>(i, j);
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
        return this.getName().equals("red");
    }

    public boolean isBlack() {
        return this.getName().equals("black");
    }

    public boolean isMan() {
        return this.getActionCommand().equals("man");
    }
}
