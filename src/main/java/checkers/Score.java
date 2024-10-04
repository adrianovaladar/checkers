package checkers;

import javax.swing.*;

public class Score extends JLabel {
    public void show(String redName, String blackName, int redWins, int blackWins) {
        this.setText("<html>" +
                "<span style='color:red;'>" + redName + " " + redWins + "</span>" +
                " <span style='font-weight:normal;'>-</span> " +
                "<span style='color:black;'>" + blackWins + " " + blackName + "</span>" +
                "</html>");
    }
}
