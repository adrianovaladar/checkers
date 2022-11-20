
import java.awt.Color;
import javax.swing.JLabel;

public class PlayerTurn extends JLabel {

    public PlayerTurn() {
        this.setText("Red Turn");
        this.setForeground(new Color(255, 0, 0));
    }

    public void changePlayerTurn(String player) {
        if (player == "black") {
            this.setText("Black Turn");
            this.setForeground(new Color(0, 0, 0));

        } else if (player == "red") {
            this.setText("Red Turn");
            this.setForeground(new Color(255, 0, 0));

        }
    }

}
