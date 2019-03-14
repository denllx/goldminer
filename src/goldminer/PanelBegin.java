package goldminer;
import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage; 
import javax.swing.*;
import java.awt.event.*;

public class PanelBegin extends JPanel{
	ImageIcon imageBegin;
	PanelPlay panelPlay;
	JButton button;
	
	public PanelBegin(PanelPlay panelPlay_) {
		panelPlay=panelPlay_;
		
		//��ʼ���ؼ�
		imageBegin=new ImageIcon("img/begin.jpg");
		button=new NewButton("img/beginbutton_����.png");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (panelPlay.isBeginning==false)
					return;
				if (e.getSource()==button)
				{
					//֪ͨ�Է��Լ�Ҫ��ʼ��Ϸ
					panelPlay.gc.c.wantBegin();
					
				}
			}
		});
		
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Dimension size=this.getSize();
		g.drawImage(imageBegin.getImage(), 0,0,size.width,size.height,null);	
		button.setLocation(14,17);
		button.setFocusable(false);
		this.add(button);
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(imageBegin.getIconWidth(),imageBegin.getIconHeight());
	}
	

}
