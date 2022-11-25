
import java.awt.Color;
import javax.swing.JLabel;

public class Wins extends JLabel {

    public Wins(int redWins, int blackWins) {
        this.setText("Red " + redWins + " - " + blackWins + " Black");
        this.setForeground(new Color(0, 0, 0));
    }

    public void changeWins(int redWins, int blackWins) {
        this.setText("Red " + redWins + " - " + blackWins + " Black");
    }
}
