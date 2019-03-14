package goldminer;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class PanelBuy extends JPanel{
	PanelPlay panelPlay;
	ImageIcon imageBuy;
	JButton buttonWater;//升力水
	JButton buttonNextLevel;
	int waterValue=154;
	
	
	public PanelBuy(PanelPlay panelPlay_) {
		panelPlay=panelPlay_;
		imageBuy=new ImageIcon("img/buy.jpg");
		panelPlay.speedTime=1;
		//升力水按钮
		buttonWater=new NewButton("img/water.png");
		
		
		//下一关按钮
		buttonNextLevel=new JButton("Next Level");
		
		//为按钮添加监听器
		ActionMonitor monitor=new ActionMonitor();
		buttonWater.addActionListener(monitor);
		buttonNextLevel.addActionListener(monitor);
		buttonNextLevel.setBounds(440,73,100,40);
		buttonWater.setFocusable(false);
		buttonNextLevel.setFocusable(false);
		buttonNextLevel.setMargin(new Insets(0,0,0,0));
		buttonNextLevel.setFont(new Font("Times New Roman",Font.PLAIN,18));
		buttonNextLevel.setBackground(new Color(0,100,0));
		buttonNextLevel.setForeground(new Color(204,232,207));	
		
		
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(imageBuy.getImage(),0,0,this);
		buttonWater.setLocation(154,298);
		
		
		//下一关和升力水
		this.add(buttonNextLevel);
		this.add(buttonWater);
		
		
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(panelPlay.panelBegin.imageBegin.getIconWidth(),
				panelPlay.panelBegin.imageBegin.getIconHeight());
	}
	
	class ActionMonitor implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==buttonWater&&panelPlay.score>=waterValue)
			{
				//通知另一方自己买了升力水
				panelPlay.gc.c.buy();
			}
			
			else if (e.getSource()==buttonNextLevel)
			{
				nextLevel();
				//通知对方自己进入下一关
				panelPlay.gc.c.nextLevel();
			}
			

		}
	}
	

	
	//进入下一关
	public void nextLevel()
	{
		//关数+1
		panelPlay.level+=1;
		panelPlay.panelGoal.setLevel(panelPlay.level);
		//进入goal界面
		panelPlay.panelGoal.startTimer();
		panelPlay.cal.show(panelPlay,"goal");
		panelPlay.repaint();
	}
}
