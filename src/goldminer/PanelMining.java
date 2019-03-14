package goldminer;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.math.*;
import java.util.*;
import javax.swing.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PanelMining extends JPanel implements ActionListener{
	PanelPlay panelPlay;
	GoldMinerClient gc;
	ImageIcon imageMining=new ImageIcon("img/mining.png");;
	int HookSize=10,x1,x2,y1=60,y2=60;//1是我，2是对方
	double angle=Math.PI*2/3;//钩子的大小，两个人的坐标,钩子的线和钩子的夹角
	double currentAngle1,currentAngle2;//当前钩子和x轴正方向夹角
	double currentx1=x1,currentx2=x2,currenty1=y1,currenty2=y2;
	Image imagehook=new ImageIcon("img/hook.png").getImage();
	Image imagehookLeft=new ImageIcon("img/hook_catch_left.png").getImage();
	Image imagehookRight=new ImageIcon("img/hook_catch_right.png").getImage();
	String currentLoad1="",currentLoad2="";//1,2此时装载的物体，格式为"种类：编号"
	
	int direct1,direct2;//1向左，-1向右
	Date startTime;
	Date nowTime;
	Timer timer;
	double timeScore1,timeScore2;//物品被抓到后显示0。5秒物品价值
	int dispScore1=0,dispScore2=0;//显示被抓到物品的价值，若无物品为0
	int level;//关数
	double timeLeft=60;
	
	ArrayList<Stone> stones=new ArrayList<Stone>();
	ArrayList<Gold> golds=new ArrayList<Gold>();
	ArrayList<Unknown> unknowns=new ArrayList<Unknown>(); 
	ArrayList<Pig> pigs=new ArrayList<Pig>();
	int w=imageMining.getIconWidth();
	int h=imageMining.getIconHeight();
	
	JButton buttonStop;
	JButton buttonContinue;
	ImageIcon iconStop,iconContinue;
	
	
	public SoundPlayer sound=new SoundPlayer();

	int[][] stoneX= {
			{w/5,w*4/5,w/2,w*2/3},
			{w/7,w*8/9,w*2/7,w*4/9,w*5/9,w*7/8},
			{w*2/3,w*2/3,w*3/4,w*7/8},
			{w/3,w*3/5,w*9/10,w/20},
			{w/7,w*5/6,w/3,w*37/60},
			{w/20,w/4,w/6,w*5/6},
			{w/7,w*8/9,w*2/7,w*4/9,w*7/8},
			};//石头的横坐标
	int[][] stoneY= {
			{h/3,h/3,h*8/9,h*2/3},
			{h*4/9,h/4,h*16/27,h*2/3,h*2/3,h*9/10},
			{h/2,h*3/4,h*11/20,h*3/4},
			{h*9/14,h/2,h*9/10,h*8/9},
			{h*3/5,h*3/4,h/2,h*3/4},
			{h/2,h*9/10,h/4,h*8/9},
			{h*4/9,h/4,h*16/27,h*2/3,h*9/10},
			};//石头的纵坐标
	
	int[][] goldSize= {
			{1,1,1,2,2,3,3},
			{1,1,1,1,1,2,2,3},
			{1,1,2,2,3},
			{1,1,2,3,3},
			{1,3,3,3,3},
			{1,1,2,3,3},
			{1,1,1,1,1,2,3}
			};//每关的金子大小
	
	int[][] goldX= {
			{w/4,w*2/3,w/3,w/5,w*19/20,w/5,w/2+w/16},
			
			{w/5,w*5/12,w/2,w*5/6,w*9/10,w/10,w*9/10,w/10},
			
			{w/5,w/5,w/6,w*11/40,w*7/12},
			
			{w,w*2/3,w/2,w*2/5,w*3/5},
			
			{w/5,0,w*2/5,w*5/6,w/10},
			
			{w*9/10,w*5/6,w*3/4,w/3,w*8/9},
			
			{w/5,w*5/12,w/2,w*5/6,w*9/10,w/10,w/10},
			
			};
	
	int[][] goldY=  {
			{h/3,h/3,h/2,h*9/10,h*5/6,h/2+h/16,h*3/4},
			
			{h*2/3,h/2,h/3,h/5,h/2,h/3,h*8/10,h*4/5},
			
			{h/6,h*8/27,h*11/20,h*3/4,h*8/9},
			
			{h/4,h*11/20,h/2,h*8/9,h/3},
			
			{h/3,h/2-h/20,h*3/4,h*3/4,h/5},
			
			{h/3,h*5/6,h/2,h/4,h*4/5},
			
			{h*2/3,h/2,h/3,h/5,h/2,h/3,h*4/5}
			
			};
	//不明物的横坐标
	int[][] unknownX= {
			{w/2+w/16,w*4/5},
			{w/2,w*4/5},
			{w/6,w*3/4,w*7/8},
			{w*3/4,w*7/9},
			{w*3/4,0},
			{w/2,w/10},
			{w/2,w*4/5}
			};
	
	//不明物的纵坐标
	int[][] unknownY= {
			{h/2-h/16,h/3+h/16},
			{h/2,h/3},
			{h/3,h*3/4,h/3},
			{h*5/6,h*3/4},
			{h*3/5,h*2/3},
			{h*2/3,h*9/10},
			{h/2,h/3}
	};
	
	//不明物的价值
	int[][] unknownValue= {
			{50,203,364},
			{100,222},
			{263,102,212},
			{891,12},
			{462,132},
			{912,134},
			{302,38}
	};
	//不明物的速度
	int[][] unknownSpeed= {
			{3,5,2},
			{2,4},
			{1,3,4},
			{5,3},
			{3,1},
			{2,3},
			{2,2}
	};
	
	//猪的横坐标
	int[][] pigX= {
			{0,1,0},
			{0,0,1},
			{0,1,1},
			{0,0,1},
			{0,0,0},
			{0,0,1},
			{1,1,1}
	};
	//猪的纵坐标
	int[][] pigY= {
			{h/3,h/2,h*5/6},
			{h/2,h/4,h/5},
			{h/5,h*69/88,h*5/8},
			{h/5,h/3-h/20,h*2/3},
			{h/5,h*7/10,h/2},
			{h*5/12,h/5,h*23/30},
			{h/2,h/4,h/5}
	};
	
	
	boolean isSearching1=false,isSearching2=false;//钩子伸出
	boolean isBacking1=false,isBacking2=false;//钩子收回
	
	double searchSpeed;//钩子伸出的速度
	double backSpeed1,backSpeed2;//钩子收回的速度，根据勾到的东西重量而定
	
	
	public PanelMining(PanelPlay panelPlay_,GoldMinerClient gc_)
	{
		gc=gc_;
		panelPlay=panelPlay_;
		timer=new Timer(100,this);
		level=panelPlay.level;
		searchSpeed=5*panelPlay.speedTime;
		
		//键盘监听器
		KeyAdapter keyProcessor=new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				//我按下向下键
				
				if (e.getKeyCode()==KeyEvent.VK_DOWN) {
					//我放钩子
					if (isBacking1==false)
					{
						if (isSearching1==false)
						{
							//通知对方我放钩子
							panelPlay.gc.c.go();
							//放音乐
						}
						
					}
				}
				
			}
		};
		Font fontbtn=new Font("Times New Roman",Font.BOLD,15);
		buttonStop=new JButton("Pause");
		buttonStop.setMargin(new Insets(0,0,0,0));
		buttonStop.setBackground(new Color(255,215,0));
		buttonStop.setFont(fontbtn);
		
		buttonContinue=new JButton("On");
		buttonContinue.setMargin(new Insets(0,0,0,0));
		buttonContinue.setBackground(new Color(255,215,0));
		buttonContinue.setFont(fontbtn);
		
		buttonStop.addActionListener(this);
		buttonContinue.addActionListener(this);
		
		panelPlay.gc.addKeyListener(keyProcessor);
		repaint();
		
		//向服务器发送下棋消息
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
		
		//背景
		g.drawImage(imageMining.getImage(),0,0,this);
		
		//标出谁是我
		g2.setFont(new Font("Times New Roman",Font.BOLD,20));
		g2.setColor(Color.WHITE);
		g2.drawString("Me", x1, y1-50);
		
		//钩子线
		Line2D line;
		g2.setColor(Color.BLACK);
		Point2D p01=new Point2D.Double(x1,y1);
		Point2D p02=new Point2D.Double(x2,y2);
		Point2D tempp;
		
		//在原地打转
		if (isSearching1==false&&isBacking1==false)
			drawHook(g2,x1,y1,currentAngle1,isBacking1);
		if (isSearching2==false&&isBacking2==false)
			drawHook(g2,x2,y2,currentAngle2,isBacking2);
		
		//伸出钩子
		if (isSearching1==true)
		{
			double tempx1=currentx1+(searchSpeed*Math.cos(currentAngle1));
			double tempy1=currenty1+(searchSpeed*Math.sin(currentAngle1));
			
			tempp=new Point2D.Double(tempx1,tempy1);
			line=new Line2D.Double(p01,tempp);
			g2.setStroke(new BasicStroke(1.0f));
			g2.draw(line);
			drawHook(g2,tempx1,tempy1,currentAngle1,isBacking1);
			currentx1=tempx1;
			currenty1=tempy1;
		}
		
		if (isSearching2==true)
		{
			double tempx2=currentx2+(searchSpeed*Math.cos(currentAngle2));
			double tempy2=currenty2+(searchSpeed*Math.sin(currentAngle2));
			tempp=new Point2D.Double(tempx2,tempy2);
			line=new Line2D.Double(p02,tempp);
			g2.setStroke(new BasicStroke(1.0f));
			g2.draw(line);
			
			
			drawHook(g2,tempx2,tempy2,currentAngle2,isBacking2);
			currentx2=tempx2;
			currenty2=tempy2;
		}
			
		//回收钩子
		if (isBacking1==true)
		{
			double tempx1=currentx1-backSpeed1*Math.cos(currentAngle1);
			double tempy1=currenty1-backSpeed1*Math.sin(currentAngle1);
			tempp=new Point2D.Double(tempx1,tempy1);
			line=new Line2D.Double(p01,tempp);
			g2.setStroke(new BasicStroke(1.0f));
			g2.draw(line);
			drawHook(g2, tempx1, tempy1, currentAngle1,isBacking1);
			currentx1=tempx1;
			currenty1=tempy1;
		}
		
		if (isBacking2==true)
		{
			double tempx2=currentx2-(backSpeed2*Math.cos(currentAngle2));
			double tempy2=currenty2-(backSpeed2*Math.sin(currentAngle2));
			tempp=new Point2D.Double(tempx2,tempy2);
			
			line=new Line2D.Double(p02,tempp);
			g2.setStroke(new BasicStroke(1.0f));
			
			g2.draw(line);
			
			drawHook(g2,tempx2,tempy2,currentAngle2,isBacking2);
			currentx2=tempx2;
			currenty2=tempy2;
			
		}
		
		
		//画石头 金子 不明物体猪
		for (int i=0;i<stones.size();i++)
			stones.get(i).drawStone(g);
		for (int i=0;i<golds.size();i++)
			golds.get(i).drawGold(g);
		for (int i=0;i<unknowns.size();i++)
			unknowns.get(i).drawUnknown(g);
		for (int i=0;i<pigs.size();i++)
			pigs.get(i).drawPig(g);
		
		//画分数
		g2.setFont(new Font("Times New Roman",Font.BOLD,30));
		g2.setColor(new Color(0,100,0));
		g2.drawString("$"+panelPlay.score1, x1, y1+60);
		g2.drawString("$"+panelPlay.score2, x2, y2+60);
		
		//画时间
		//nowTime=new Date();
		g2.setFont(new Font("Times New Roman",Font.BOLD,20));
		g2.setColor(new Color(250,128,10));
		g2.drawString("Time:"+(int)timeLeft, 500, 20);
		
		
		//画关数
		g2.drawString("Level:"+level, 500, 40);
	
		
		//画钱数和目标金钱
		g2.drawString("Money:"+"$"+panelPlay.score, 20, 20);
		g2.drawString("Goal:"+"$"+panelPlay.panelGoal.score,20,40);
		
		g2.setFont(new Font("Times New Roman",Font.BOLD,20));
		g2.setColor(new Color(0,100,0));
		//画单个物品的价值
		if (timeScore1-timeLeft<=0.5&&timeScore1>=timeLeft&&dispScore1>0)
			g2.drawString("$"+dispScore1, x1-50, y1);
		if (timeScore2-timeLeft<=0.5&&timeScore2>=timeLeft&&dispScore2>0)
			g2.drawString("$"+dispScore2, x2+50, y2);
		//暂停继续
		buttonStop.setBounds(520,h/11*2-15,50,20);
		buttonContinue.setBounds(580,h/11*2-15,50,20);
		buttonStop.setFocusable(false);
		buttonContinue.setFocusable(false);
		this.add(buttonStop);
		this.add(buttonContinue);

	}
	
	public void startTimer() 
	{
		System.out.println("mine starttimer:"+System.currentTimeMillis());
		startTime=new Date();//开始时间
		timer.start();
		timeLeft=60;
		//设置关闭键不可用
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		
			
		//调整钩子的角度
		if (isBacking1==false&&isSearching1==false)
		{
			
			currentAngle1+=direct1*Math.PI/40;
			if (currentAngle1>=Math.PI||currentAngle1<=0)
				direct1=-direct1;
		}
		if (isBacking2==false&&isSearching2==false)
		{
			
			currentAngle2+=direct2*Math.PI/40;
			if (currentAngle2>=Math.PI||currentAngle2<=0)
				direct2=-direct2;
		}
		
		System.out.println("perform:"+System.currentTimeMillis()+" currentangle1:"+currentAngle1+" currentAngle2:"+currentAngle2);
		
		//碰壁
		if (currentx1<=10||currentx1>=imageMining.getIconWidth()-10||
				currenty1>=imageMining.getIconHeight()-10)
		{
			//回收钩子1
			isSearching1=false;
			isBacking1=true;
			backSpeed1=5*panelPlay.speedTime;
		}
		
		if (currentx2<=10||currentx2>=imageMining.getIconWidth()-10||
				currenty2>=imageMining.getIconHeight()-10)
		{
			//回收钩子1
			isSearching2=false;
			isBacking2=true;
			backSpeed2=5*panelPlay.speedTime;
		}
		
		//判断是否按下暂停或启动键
		//System.out.println("timer "+panelPlay.isLeft+" "+timeLeft);
		if (e.getSource()==buttonStop)
		{
			panelPlay.gc.c.pause();
		}
		if (e.getSource()==buttonContinue)
		{
			panelPlay.gc.c.goon();
		}
		//获取当前系统时间
		timeLeft-=0.1;
		
		
		//时间耗尽，通知得分
		if (timeLeft<=0)
		{
			timer.stop();
			panelPlay.cal.next(panelPlay);
			panelPlay.panelResult.startTimer();
			panelPlay.panelResult.setCurrentScore(panelPlay.score);
			panelPlay.repaint();
		}
		
		//抓到物体
		//抓到石头
		for (int i=0;i<stones.size();i++)
		{
			if (stones.get(i).getCaught(currentx1,currenty1,HookSize,currentAngle1)
					&&stones.get(i).isCatched==false
					&&isBacking1==false)
			{
				stones.get(i).isCatched=true;
				stones.get(i).master=1;
				isSearching1=false;
				isBacking1=true;
				backSpeed1=panelPlay.speedTime;
				currentLoad1="stone:"+i;
				sound.loadSound("sound/catch.wav");
				sound.playSound();
				dispScore1=stones.get(i).value;
			}
			
			if (stones.get(i).getCaught(currentx2,currenty2,HookSize,currentAngle2)
					&&stones.get(i).isCatched==false
					&&isBacking2==false)
			{
				stones.get(i).isCatched=true;
				stones.get(i).master=2;
				isSearching2=false;
				isBacking2=true;
				backSpeed2=panelPlay.speedTime;
				currentLoad2="stone:"+i;
				sound.loadSound("sound/catch.wav");
				sound.playSound();
				dispScore2=stones.get(i).value;
			}
		}
	
		for (int i=0;i<golds.size();i++)
		{

			if (golds.get(i).getCaught(currentx1,currenty1,HookSize,currentAngle1)
					&&golds.get(i).isCatched==false
					&&isBacking1==false)
			{
				golds.get(i).isCatched=true;
				golds.get(i).master=1;
				isSearching1=false;
				isBacking1=true;
				backSpeed1=golds.get(i).backSpeed*panelPlay.speedTime;
				currentLoad1="gold:"+i;
				sound.loadSound("sound/catch.wav");
				sound.playSound();
				dispScore1=golds.get(i).value;
			}
			
			if (golds.get(i).getCaught(currentx2, currenty2, HookSize, currentAngle2)
					&&golds.get(i).isCatched==false
					&&isBacking2==false)
			{
				golds.get(i).isCatched=true;
				golds.get(i).master=2;
				isSearching2=false;
				isBacking2=true;
				backSpeed2=golds.get(i).backSpeed*panelPlay.speedTime;
				currentLoad2="gold:"+i;
				sound.loadSound("sound/catch.wav");
				sound.playSound();
				
				dispScore2=golds.get(i).value;
			}
		}
		
		for (int i=0;i<unknowns.size();i++)
		{
			if (unknowns.get(i).getCaught(currentx1, currenty1,HookSize, currentAngle1)
					&&unknowns.get(i).isCatched==false
					&&isBacking1==false)
			{
				unknowns.get(i).isCatched=true;
				unknowns.get(i).master=1;
				isSearching1=false;
				isBacking1=true;
				backSpeed1=unknowns.get(i).backSpeed*panelPlay.speedTime;
				currentLoad1="unknown:"+i;
				sound.loadSound("sound/catch.wav");
				sound.playSound();
				
				dispScore1=unknowns.get(i).value;
			}
			
			if (unknowns.get(i).getCaught(currentx1,currenty1,HookSize,currentAngle2)
					&&unknowns.get(i).isCatched==false
					&&isBacking2==false)
			{
				unknowns.get(i).isCatched=true;
				unknowns.get(i).master=2;
				isSearching2=false;
				isBacking2=true;
				backSpeed2=unknowns.get(i).backSpeed*panelPlay.speedTime;
				currentLoad2="unknown:"+i;
				sound.loadSound("sound/catch.wav");
				sound.playSound();
				
				dispScore2=unknowns.get(i).value;
			}
		}
		
		for (int i=0;i<pigs.size();i++)
		{
			if (pigs.get(i).getCaught(currentx1, i, HookSize, currentAngle1)
					&&pigs.get(i).isCatched==false
					&&isBacking1==false)
			{
				pigs.get(i).isCatched=true;
				pigs.get(i).master=1;
				isSearching1=false;
				isBacking1=true;
				backSpeed1=pigs.get(i).backSpeed*panelPlay.speedTime;
				currentLoad1="pig:"+i;
				sound.loadSound("sound/catch.wav");
				sound.playSound();
				
				dispScore1=pigs.get(i).value;
			}
			
			if (pigs.get(i).getCaught(currentx1, i, HookSize, currentAngle2)
					&&pigs.get(i).isCatched==false
					&&isBacking2==false)
			{
				pigs.get(i).isCatched=true;
				pigs.get(i).master=2;
				isSearching2=false;
				isBacking2=true;
				backSpeed2=pigs.get(i).backSpeed*panelPlay.speedTime;
				currentLoad2="pig:"+i;
				sound.loadSound("sound/catch.wav");
				sound.playSound();
				
				dispScore2=pigs.get(i).value;
			}
		}
		
		//物体随着钩子动
		for (int i=0;i<golds.size();i++)
		{
			if (golds.get(i).isCatched==true)
			{
				if (golds.get(i).master==1)
				{
					golds.get(i).back(backSpeed1,currentAngle1);
				}
				if (golds.get(i).master==2)
				{
					golds.get(i).back(backSpeed2,currentAngle2);
				}
				
			}
				
		}
		
		for (int i=0;i<stones.size();i++)
		{
			if (stones.get(i).isCatched==true)
			{
				if (stones.get(i).master==1)
				{
					stones.get(i).back(backSpeed1,currentAngle1);
					
				}
				else if (stones.get(i).master==2)
				{
					stones.get(i).back(backSpeed2,currentAngle2);
				}
			
			}
		}
		
		for (int i=0;i<unknowns.size();i++)
		{
			if (unknowns.get(i).isCatched==true)
			{
				if (unknowns.get(i).master==1)
				{
					unknowns.get(i).back(backSpeed1, currentAngle1);
					
				}
				else if (unknowns.get(i).master==2)
				{
					unknowns.get(i).back(backSpeed2,currentAngle2);
				}
			}
		}
		
		for (int i=0;i<pigs.size();i++)
		{
			if (pigs.get(i).isCatched==true)
			{
				if (pigs.get(i).master==1)
				{
					pigs.get(i).back(backSpeed1,currentAngle1);
					
				}
				else if (pigs.get(i).master==2)
				{
					pigs.get(i).back(backSpeed2,currentAngle2);
				}
			}
			
			//水平运动
			else
			{
				pigs.get(i).move();
			}
		}
		

		
		//回到原点
		if (isBacking1==true&&
					currentx1<x1+20
					&&currentx1>x1-20
					&&currenty1<y1+20
					&&currenty1>y1-20)
		{
				isBacking1=false;
				int value1=loadValue(1);
				panelPlay.score1+=value1;
				currentLoad1="";
				//放音乐
				sound.loadSound("sound/arrive.wav");
				sound.playSound();
				//显示价值
				if (value1>0)
					timeScore1=timeLeft;
				
		}
		if (isBacking2==true
				&&currentx2<x2+20
				&&currentx2>x2-20
				&&currenty2<y2+20
				&&currenty2>y2-20)
		{
			isBacking2=false;
			int value2=loadValue(2);
			panelPlay.score2+=value2;
			currentLoad2="";
			//放音乐
			sound.loadSound("sound/arrive.wav");
			sound.playSound();
			if (value2>0)
				timeScore2=timeLeft;
		}
		//更新总成绩
		panelPlay.score=panelPlay.score1+panelPlay.score2;
			
		
		
		this.repaint();
	}
	
	
	

	//画钩子
	public void drawHook(Graphics2D g2,double x0,double y0,double tempangle,boolean isBacking) {
		Point2D p0,p1,p2,p3,p4,p5;
		Line2D line;
		//x0,y0是钩子的起点，size是钩子的大小,angle是钩子和x轴正方向的夹角，在30-150度之间
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1.0f)); 
		//画线
		p0=new Point2D.Double(x0,y0);
		double tempx1=x0+(HookSize*Math.cos(tempangle));
		double tempy1=y0+(HookSize*Math.sin(tempangle));
		p1=new Point2D.Double(tempx1,tempy1);
		line=new Line2D.Double(p0,p1);
		g2.draw(line);
		g2.setStroke(new BasicStroke(3.0f));
		double tempx2=tempx1-(HookSize*Math.sin(Math.PI/2+tempangle-angle));
		double tempy2=tempy1+(HookSize*Math.cos(Math.PI/2+tempangle-angle));
		p2=new Point2D.Double(tempx2,tempy2);
		line=new Line2D.Double(p1,p2);
		g2.draw(line);
		
		double tempx3=tempx2+(HookSize*Math.cos(tempangle));
		double tempy3=tempy2+(HookSize*Math.sin(tempangle));
		p3=new Point2D.Double(tempx3,tempy3);
		line=new Line2D.Double(p2,p3);
		g2.draw(line);
		
		double tempx4=tempx1+(HookSize*Math.cos(angle+tempangle-Math.PI));
		double tempy4=tempy1+(HookSize*Math.sin(angle+tempangle-Math.PI));
		p4=new Point2D.Double(tempx4,tempy4);
		line=new Line2D.Double(p1,p4);
		g2.draw(line);
		
		double tempx5=tempx4+(HookSize*Math.cos(tempangle));
		double tempy5=tempy4+(HookSize*Math.sin(tempangle));
		p5=new Point2D.Double(tempx5,tempy5);
		line=new Line2D.Double(p4,p5);
		g2.draw(line);
	}
	
	//恢复开始设置
	public void reset() {
		//System.out.println("reset:"+System.currentTimeMillis());
		x1=panelPlay.isLeft?255:355;
		x2=panelPlay.isLeft?355:255;
		//currentAngle1=panelPlay.isLeft?Math.PI:0;
		//currentAngle2=panelPlay.isLeft?0:Math.PI;
		currentAngle1=Math.PI/2;
		currentAngle2=Math.PI/2;
		direct1=panelPlay.isLeft?-1:1;
		direct2=panelPlay.isLeft?1:-1;
		currentx1=x1;
		currenty1=y1;
		currentx2=x2;
		currenty2=y2;
		
		
		level=panelPlay.level;
		isSearching1=false;
		isBacking1=false;
		isSearching2=false;
		isBacking2=false;
		dispScore1=0;
		dispScore2=0;
		
		//重置石头、金子、不明物、动物布局
		stones.clear();
		golds.clear();
		unknowns.clear();
		pigs.clear();
		
		//摆放石头、金子、不明物体
		if (level<=3)
		{
			for (int i=0;i<stoneX[level-1].length;i++)
			{
				Stone stone=new Stone(this,stoneX[level-1][i],stoneY[level-1][i]);
				stones.add(stone);
			}
			
			for (int i=0;i<goldX[level-1].length;i++)
			{
				Gold gold=new Gold(this,goldX[level-1][i],goldY[level-1][i],goldSize[level-1][i]);
				golds.add(gold);
			}
			
			for (int i=0;i<unknownX[level-1].length;i++)
			{
				Unknown unknown=new Unknown(this,unknownX[level-1][i],unknownY[level-1][i],
						unknownValue[level-1][i],unknownSpeed[level-1][i]);
				//向服务器索取value
				unknowns.add(unknown);
			}
			
			for (int i=0;i<pigX[level-1].length;i++)
			{
				Pig pig=new Pig(this,pigX[level-1][i],pigY[level-1][i]);
				pigs.add(pig);
			}
		}
		else
		{
			for (int i=0;i<stoneX[level%4+3].length;i++)
			{
				Stone stone=new Stone(this,stoneX[level%4+3][i],stoneY[level%4+3][i]);
				stones.add(stone);
			}
			
			for (int i=0;i<goldX[level%4+3].length;i++)
			{
				Gold gold=new Gold(this,goldX[level%4+3][i],goldY[level%4+3][i],
						goldSize[level%4+3][i]);
				golds.add(gold);
			}
			for (int i=0;i<unknownX[level%4+3].length;i++)
			{
				Unknown unknown=new Unknown(this,unknownX[level%4+3][i],unknownY[level%4+3][i],
						unknownValue[level%4+3][i],unknownSpeed[level%4+3][i]);
				unknowns.add(unknown);
			}
			
			for (int i=0;i<pigX[level%4+3].length;i++)
			{
				Pig pig=new Pig(this,pigX[level%4+3][i],pigY[level%4+3][i]);
				pigs.add(pig);
			}
			
		}
		
	
		
	}
	
	public int loadValue(int num)
	{
		String word=(num==1)?currentLoad1:currentLoad2;
		String other=(num==1)?currentLoad2:currentLoad1;
		if (word.equals(""))
			return 0;
		String[] words=word.split(":");
		String[] otherwords=other.split(":");
		String type=words[0];//种类
		int i=Integer.parseInt(words[1]);//编号
		
		int value = 0;
		if (words[0].equals("gold"))
		{
			value=golds.get(i).value;
			
			//修改对方的currentload编号
			if (!other.equals("")&&otherwords[0].equals("gold"))
			{
				String otherType=otherwords[0];//对方的种类
				int j=Integer.parseInt(otherwords[1]);//对方的编号
				if (j>i)
				{
				if (num==1)
					currentLoad2="gold:"+(j-1);
				else
					currentLoad1="gold:"+(j-1);
				}
			}
			golds.remove(i);
		}
		else if (words[0].equals("stone"))
		{
			value=stones.get(i).value;
			
			//修改对方的currentload编号
			if (!other.equals("")&&otherwords[0].equals("stone"))
			{
				int j=Integer.parseInt(otherwords[1]);//对方的编号
				if (j>i)
				{
				if (num==1)
					currentLoad2="stone:"+(j-1);
				else
					currentLoad1="stone:"+(j-1);
				}
			}
			stones.remove(i);
			
		}
		else if (words[0].equals("unknown"))
		{
			value=unknowns.get(i).value;
			
			//修改对方的currentload编号
			if (!other.equals("")&&otherwords[0].equals("unknown"))
			{
				int j=Integer.parseInt(otherwords[1]);//对方的编号
				if (j>i)
				{
				if (num==1)
					currentLoad2="unknown:"+(j-1);
				else
					currentLoad1="unknown:"+(j-1);
				}
			}
			unknowns.remove(i);
		}
		else if (words[0].equals("pig"))
		{
			value=pigs.get(i).value;
			
			//修改对方的currentload编号
			if (!other.equals("")&&otherwords[0].equals("pig"))
			{
				int j=Integer.parseInt(otherwords[1]);//对方的编号
				if (j>i)
				{
				if (num==1)
					currentLoad2="pig:"+(j-1);
				else
					currentLoad1="pig:"+(j-1);
				}
			}
			pigs.remove(i);
		}
		
		return value;
		
		
	}
	
	public void pauseGame()
	{
		timer.stop();
	}
	
	public void continueGame()
	{
		timer.start();
	}
	

}
