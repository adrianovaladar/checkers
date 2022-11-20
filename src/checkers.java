
import java.awt.Color;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class checkers extends JButton {

    int i, j;
    public void king_red(){
            this.setActionCommand("king_red");

            try {
                Image img1 = ImageIO.read(getClass().getResource("resources/red_king.png"));
                this.setIcon(new ImageIcon(img1));

            } catch (Exception ex) {
                System.out.println(ex);
            }        
    }
        public void king_black(){
            this.setActionCommand("king_black");

            try {
                Image img1 = ImageIO.read(getClass().getResource("resources/black_king.png"));
                this.setIcon(new ImageIcon(img1));

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
                Image img1 = ImageIO.read(getClass().getResource("resources/black_men.png"));
                this.setIcon(new ImageIcon(img1));

            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (i > 4) {
            this.setActionCommand("red");

            try {

                Image img2 = ImageIO.read(getClass().getResource("resources/red_men.png"));
                this.setIcon(new ImageIcon(img2));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else {
            this.setActionCommand("");
            this.setIcon(null);
        }
    }
}
