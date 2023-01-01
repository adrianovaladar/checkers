import javax.swing.*;
import java.awt.*;

public class Score extends JLabel {

    public Score() {
        this.setForeground(new Color(0, 0, 0));
    }

    public void show(String redName, String blackName, int redWins, int blackWins) {
        this.setText(redName + " " + redWins + " - " + blackWins + " " + blackName);
    }
}
