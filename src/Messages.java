
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
public class Messages extends JTextArea{
    public Messages(){
        this.setEditable(false);
        this.append("Game started. Select a checker to view the options\n");
    }
}
