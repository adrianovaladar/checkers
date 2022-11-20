
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
public class wins extends JLabel {

    public wins() {
        this.setText("Red 0 - 0 Black");
        this.setForeground(new Color(0, 0, 0));
    }
    public void changeWins(int wins_red, int wins_black){
        this.setText("Red "+wins_red+" - "+wins_black+" Black");
    }
}
