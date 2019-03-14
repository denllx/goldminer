package goldminer;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;


public class PanelGoal extends JPanel{
	PanelPlay panelPlay;
	ImageIcon imageGoal;
	int level;
	int score;
	public SoundPlayer sound=new SoundPlayer();
	
	public PanelGoal(PanelPlay panelPlay_,int level_)
	{
		panelPlay=panelPlay_;
		level=level_;
		imageGoal=new ImageIcon("img/goal.jpg");
	}
	
	public void setLevel(int level_)
	{
		level=level_;
		if (level<=10)
			score=135*level*level+140*level+375;
		else 
			score=2705*level-11775;
	}
	
	
	public Dimension getPreferredSize()
	{
		return new Dimension(panelPlay.panelBegin.imageBegin.getIconWidth(),
				panelPlay.panelBegin.imageBegin.getIconHeight());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(imageGoal.getImage(),0,0,this);
		
		//显示分数
		g.setFont(new Font("微软雅黑",Font.BOLD,50));
		g.setColor(new Color(250,128,10));
		g.drawString("本关目标分：", 150, 180);
		g.setColor(new Color(153,204,51));
		g.drawString("$"+score,150,250);
		
	}
	
	public void startTimer()
	{
		new TimerGoal(3);
		sound.loadSound("sound/begin.wav");
		sound.playSound();
	}
	
	class TimerGoal{
		Timer timer;
		public TimerGoal(int time) {
			timer=new Timer();
			timer.schedule(new TimerGoalTask(), time*1000);
		}
	}
	
	class TimerGoalTask extends TimerTask{
		public void run() {
			panelPlay.cal.next(panelPlay);
			panelPlay.panelMining.reset();
			panelPlay.panelMining.level=level;
			panelPlay.panelMining.startTimer();
			panelPlay.repaint();
			//System.out.println(panelPlay.isLeft+" before cancel");
			cancel();
			//System.out.println(panelPlay.isLeft+" after cancel");
		}
	}
}
