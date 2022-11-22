
import java.awt.Color;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.*;

public class checkers extends JButton {

    int i, j;
    Icon icon;

    public void king_red() {
        this.setActionCommand("king_red");
        Image img;

        try {
            img = ImageIO.read(getClass().getResource("resources/red_king.png"));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void king_black() {
        this.setActionCommand("king_black");
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
        Image img;
        try {
            img = ImageIO.read(getClass().getResource("resources/black_man.png"));
            icon = new ImageIcon(img);
            this.setIcon(icon);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public checkers(int i, int j) {
        this.i = i;
        this.j = j;
        this.setBackground(new Color(158, 76, 16));

        if (i < 3) {
            this.setActionCommand("black");

            try {
                Image img1 = ImageIO.read(getClass().getResource("resources/black_man.png"));
                this.setIcon(new ImageIcon(img1));

            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (i > 4) {
            this.setActionCommand("red");

            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/red_man.png"));
                this.setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else {
            this.setActionCommand("");
            this.setIcon(null);
        }
    }

    public checkers() {

    }
}
