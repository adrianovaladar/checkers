import javax.swing.*;
import java.awt.*;

public class Score extends JLabel {

    public Score(int redWins, int blackWins) {
        this.setText("Red " + redWins + " - " + blackWins + " Black");
        this.setForeground(new Color(0, 0, 0));
    }

    public void change(int redWins, int blackWins) {
        this.setText("Red " + redWins + " - " + blackWins + " Black");
    }
}
