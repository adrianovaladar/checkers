
import java.awt.Color;
import javax.swing.JLabel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author TOSHIBA
 */
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
