package checkers;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    public Board() {
        this.setLayout(new GridLayout(9, 9));
        this.setPreferredSize(new Dimension(560, 560));
        this.setMinimumSize(new Dimension(560, 560));
    }
}
