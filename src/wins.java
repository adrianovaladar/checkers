
import java.awt.Color;
import javax.swing.JLabel;

public class wins extends JLabel {

    public wins() {
        this.setText("Red 0 - 0 Black");
        this.setForeground(new Color(0, 0, 0));
    }

    public void changeWins(int wins_red, int wins_black) {
        this.setText("Red " + wins_red + " - " + wins_black + " Black");
    }
}
