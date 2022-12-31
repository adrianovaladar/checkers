import javax.swing.*;

public class Message extends JTextArea {
    public Message() {
        this.setEditable(false);
        this.append("Game started. Select a checker to view the options\n");
    }
}
