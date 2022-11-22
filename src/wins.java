
import java.awt.Color;
import javax.swing.JLabel;

public class wins extends JLabel {

    public wins(int redWins, int blackWins) {
        this.setText("Red " + redWins + " - " + blackWins + " Black");
        this.setForeground(new Color(0, 0, 0));
    }

    public void changeWins(int redWins, int blackWins) {
        this.setText("Red " + redWins + " - " + blackWins + " Black");
    }
}
