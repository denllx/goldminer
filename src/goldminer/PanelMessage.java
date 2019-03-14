package goldminer;
import java.awt.*;
import javax.swing.*;

public class PanelMessage extends JPanel{
	public JTextArea messageArea;
	public PanelMessage()
	{
		this.setLayout(new BorderLayout());
		messageArea=new JTextArea("",16,26);
		this.add(messageArea,BorderLayout.CENTER);
		
	}
}
