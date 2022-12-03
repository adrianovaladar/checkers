import javax.swing.*;

public class Messages extends JTextArea {
    public Messages() {
        this.setEditable(false);
        this.append("Game started. Select a checker to view the options\n");
    }
}
