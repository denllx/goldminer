package goldminer;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


//ͼ�ν��沿�֣�����5����������ʼbegin����Ϸ��mining,��֪����result,Ŀ���Ԥ��goal
public class PanelPlay extends JPanel{
	
	boolean willBegin=false;//�Ƿ��ҵ��˻��
	boolean isBeginning=false;//�Ƿ�ʼ��Ϸ
	boolean isTellingGoal=false;//�Ƿ����ڸ�֪Ŀ���
	boolean isTellingResult=false;//�Ƿ����ڸ�֪����Ŀ���
	boolean isLeft;//�Լ�����ߵ���
	
	GoldMinerClient gc;//������client����
	
	PanelMining panelMining;
	PanelBegin panelBegin;
	PanelGoal panelGoal;
	PanelResult panelResult;
	PanelBuy panelBuy;
	PanelFinish panelFinish;
	CardLayout cal=new CardLayout();
	
	int level=1;
	int score1=0,score2=0,score=0;
	double speedTime=1;//�������ٶȵļ���
	
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
