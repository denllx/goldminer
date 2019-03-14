package goldminer;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class PanelFinish extends JPanel{
	ImageIcon imageFinish;
	PanelPlay panelPlay;
	JButton buttonExit;
	JButton buttonAgain;
	JButton buttonSingleRecord;
	JButton buttonDoubleRecord;
	
	
	public PanelFinish(PanelPlay panelPlay_) {
		panelPlay=panelPlay_;
		
		//初始化控件
		imageFinish=new ImageIcon("img/finish.jpg");
		
		//设置按钮
		buttonExit=new JButton("Exit");
		buttonAgain=new JButton("Again");
		buttonSingleRecord=new JButton("Single Record");
		buttonDoubleRecord=new JButton("Double Record");

		
		ActionMonitor monitor=new ActionMonitor();
		buttonExit.addActionListener(monitor);
		buttonAgain.addActionListener(monitor);
		buttonSingleRecord.addActionListener(monitor);
		buttonDoubleRecord.addActionListener(monitor);
		
		
		
	}
	
	public void updateRecord()
	{
		//记录成绩
		Date date=new Date();
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date=f.parse(f.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SingleRecord s=new SingleRecord(panelPlay.score1,date);
		DoubleRecord d=new DoubleRecord(panelPlay.score,date,panelPlay.gc.opName);
		panelPlay.gc.singleList.add(s);
		panelPlay.gc.doubleList.add(d);
		//更新文件
		try {
			PrintStream out=new PrintStream(panelPlay.gc.scoreSingleRecord);
			for (int i=0;i<min(5,panelPlay.gc.singleList.size());i++)
				out.println(panelPlay.gc.singleList.get(i).score+" "+panelPlay.gc.singleList.get(i).date+"\n");
			out=new PrintStream(panelPlay.gc.scoreDoubleRecord);
			for (int i=0;i<min(5,panelPlay.gc.doubleList.size());i++)
				out.println(panelPlay.gc.doubleList.get(i).score+" "+panelPlay.gc.doubleList.get(i).date+" with "
						+panelPlay.gc.doubleList.get(i).opName+"\n");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(imageFinish.getImage(), 0,0,this);
		buttonExit.setBounds(260,350,80,30);
		buttonAgain.setBounds(400,350,80,30);
		buttonDoubleRecord.setBounds(100, 400, 160, 30);
		buttonSingleRecord.setBounds(320, 400, 160, 30);
		
		buttonExit.setFocusable(false);
		buttonAgain.setFocusable(false);
		buttonDoubleRecord.setFocusable(false);
		buttonSingleRecord.setFocusable(false);
		
		Font font=new Font("Times New Roman",Font.BOLD,18);
		buttonExit.setFont(font);
		buttonAgain.setFont(font);
		buttonDoubleRecord.setFont(font);
		buttonSingleRecord.setFont(font);
		
		buttonExit.setBackground(new Color(0,100,0));
		buttonAgain.setBackground(new Color(0,100,0));
		buttonDoubleRecord.setBackground(new Color(0,100,0));
		buttonSingleRecord.setBackground(new Color(0,100,0));
		
		buttonExit.setForeground(Color.WHITE);
		buttonAgain.setForeground(Color.WHITE);
		buttonDoubleRecord.setForeground(Color.WHITE);
		buttonSingleRecord.setForeground(Color.WHITE);
		
		buttonAgain.setMargin(new Insets(0,0,0,0));
		buttonExit.setMargin(new Insets(0,0,0,0));
		buttonDoubleRecord.setMargin(new Insets(0,0,0,0));
		buttonSingleRecord.setMargin(new Insets(0,0,0,0));
		
		this.add(buttonExit);
		this.add(buttonAgain);
		this.add(buttonDoubleRecord);
		this.add(buttonSingleRecord);
		
		
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(imageFinish.getIconWidth(),imageFinish.getIconHeight());
	}
	
	class ActionMonitor implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==buttonAgain) {
				//通知对方准备重玩
				panelPlay.gc.c.again();
			}
			
			//选择退出游戏
			else if (e.getSource()==buttonExit)
			{
				//一方退出，另一方自动退出游戏
				panelPlay.gc.c.giveup();
			}
			
			else if (e.getSource()==buttonSingleRecord)
			{
				//显示单人最高分
				try {
					Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe single.txt");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			else if (e.getSource()==buttonDoubleRecord)
			{
				//显示双人最高分
				try {
					Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe double.txt");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
		}
	}
	
	public int min(int a,int b)
	{
		return a<b?a:b;
	}
}
