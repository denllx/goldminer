package goldminer;
import java.awt.*;

import javax.swing.*;


public class PanelControl extends JPanel{
	public JLabel labelIP=new JLabel("������IP��",JLabel.LEFT);
	public JTextField inputIP=new JTextField("127.0.0.1",12);
	public JButton buttonConnect=new JButton("��������");
	public JButton buttonJoin=new JButton("������Ϸ");
	public JButton buttonExit=new JButton("�رճ���");
	
	public PanelControl()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBackground(new Color(200,200,200));
		this.add(labelIP);
		this.add(inputIP);
		this.add(buttonConnect);
		this.add(buttonJoin);
		this.add(buttonExit);
		
	}

}
