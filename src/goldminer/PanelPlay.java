package goldminer;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


//图形界面部分，放了5个场景：开始begin，游戏中mining,告知分数result,目标分预告goal
public class PanelPlay extends JPanel{
	
	boolean willBegin=false;//是否找到了伙伴
	boolean isBeginning=false;//是否开始游戏
	boolean isTellingGoal=false;//是否正在告知目标分
	boolean isTellingResult=false;//是否正在告知本关目标分
	boolean isLeft;//自己是左边的人
	
	GoldMinerClient gc;//所属的client界面
	
	PanelMining panelMining;
	PanelBegin panelBegin;
	PanelGoal panelGoal;
	PanelResult panelResult;
	PanelBuy panelBuy;
	PanelFinish panelFinish;
	CardLayout cal=new CardLayout();
	
	int level=1;
	int score1=0,score2=0,score=0;
	double speedTime=1;//是正常速度的几倍
	
	public PanelPlay(GoldMinerClient gc_)
	{
		gc=gc_;
		panelMining=new PanelMining(this,gc);
		panelBegin=new PanelBegin(this);
		panelGoal=new PanelGoal(this,level);
		panelResult=new PanelResult(this);
		panelBuy=new PanelBuy(this);
		panelFinish=new PanelFinish(this);
		
		this.setLayout(cal);
		this.add(panelBegin,"begin");
		this.add(panelGoal,"goal");
		this.add(panelMining,"mine");
		this.add(panelResult,"result");
		this.add(panelBuy,"buy");
		this.add(panelFinish,"finish");
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
	public void reset()
	{
		level=1;
		score=0;
		score1=0;
		score2=0;
		speedTime=1;
		
	}
	
	public Dimension getPreferredSize()
	{
		System.out.println(""+panelBegin.imageBegin.getIconWidth());
		return new Dimension(637,474);
	}
	
	public void again()
	{
		reset();
		panelGoal.setLevel(level);
		panelGoal.startTimer();
		cal.show(this,"goal");
		repaint();
	}

}
