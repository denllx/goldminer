package goldminer;
import javax.swing.*;

import goldminer.PanelGoal.TimerGoal;
import goldminer.PanelGoal.TimerGoalTask;

import java.util.*;
import java.util.Timer;
import java.awt.*;


public class PanelResult extends JPanel{

	PanelPlay panelPlay;
	ImageIcon imageResult;
	String strResult;
	int currentScore;
	
	public void setCurrentScore(int currentScore_)
	{
		currentScore=currentScore_;
	}
	
	public PanelResult(PanelPlay panelPlay_) {
		panelPlay=panelPlay_;
		imageResult=new ImageIcon("img/result.jpg");

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(imageResult.getImage(),0,0,this);
		g.setFont(new Font("微软雅黑",Font.BOLD,30));
		g.setColor(new Color(254,254,65));
		
		if (currentScore<panelPlay.panelGoal.score)
		{
			strResult="你没有达到目标分！";
			panelPlay.panelFinish.updateRecord();
		}
			
		else 
			strResult="恭喜你，成功过关！！";
		g.drawString(strResult, 180, 200);

	}
	
	public Dimension getPreferredSize() {
		return new Dimension(panelPlay.panelBegin.imageBegin.getIconWidth(),
				panelPlay.panelBegin.imageBegin.getIconHeight());
	}
	
	public void startTimer()
	{
		new TimerGoal(3);
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
			System.out.println("timertask running");
			panelPlay.isTellingResult=false;
			if (currentScore>=panelPlay.panelGoal.score)
			{
				//买东西
				panelPlay.cal.next(panelPlay);
			}
			else
			{
				//结束
				panelPlay.cal.last(panelPlay);
			}
			panelPlay.repaint();
			//System.out.println(panelPlay.isLeft+" before cancel");
			cancel();
			//System.out.println(panelPlay.isLeft+" after cancel");
		}
	}
}
