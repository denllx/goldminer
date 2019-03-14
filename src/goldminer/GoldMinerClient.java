package goldminer;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.*;

public class GoldMinerClient extends JFrame{
	PanelUserList userList;
	PanelMessage message;
	PanelControl control;
	PanelChat chat;
	PanelPlay play;
	String myName;
	String opName;//伙伴的名字
	public boolean isConnected=false;//是否已经连接到服务器
	Communication c=null;//谁负责处理我的消息
	public File scoreSingleRecord=new File("single.txt");//单人成绩记录
	public File scoreDoubleRecord=new File("double.txt");//双人成绩记录
	ArrayList<SingleRecord> singleList=new ArrayList<SingleRecord>();
	ArrayList<DoubleRecord> doubleList=new ArrayList<DoubleRecord>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GoldMinerClient client=new GoldMinerClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@SuppressWarnings("unchecked")
	public GoldMinerClient()
	{
		super("黄金矿工联机版");//标题
		
		//实体化组件
		userList=new PanelUserList();
		message=new PanelMessage();
		control=new PanelControl();
		play=new PanelPlay(this);
		chat=new PanelChat();
		
		//添加监听器
		ActionMonitor monitor=new ActionMonitor();
		control.buttonConnect.addActionListener(monitor);
		control.buttonExit.addActionListener(monitor);
		control.buttonJoin.addActionListener(monitor);
		chat.buttonClear.addActionListener(monitor);
		chat.buttonSend.addActionListener(monitor);
		//取消焦点
		control.buttonConnect.setFocusable(false);
		control.buttonExit.setFocusable(false);
		control.buttonJoin.setFocusable(false);
		userList.userList.setFocusable(false);
		message.messageArea.setFocusable(false);
		
		this.setFocusable(true);
		
		//设置按钮初始装填
		control.buttonConnect.setEnabled(true);
		control.buttonExit.setEnabled(true);
		control.buttonJoin.setEnabled(false);
		
		//初始化比较器
		MyComparator myComp=new MyComparator();
		Collections.sort(singleList, myComp);
		Collections.sort(doubleList,myComp);
		
		//组装界面
		JPanel East=new JPanel(new BorderLayout());
		East.add(userList,BorderLayout.NORTH);
		East.add(message,BorderLayout.CENTER);
		East.add(chat,BorderLayout.SOUTH);
		this.add(play, BorderLayout.CENTER);
		play.setPreferredSize(new Dimension(0,0));
		play.setVisible(false);
		this.add(East,BorderLayout.EAST);
		this.add(control,BorderLayout.SOUTH);
		this.setBounds(100,100,937,574);
		
		//清空文件
		try {
			FileWriter fw1;
			fw1 = new FileWriter(scoreSingleRecord,false);
			BufferedWriter bw1=new BufferedWriter(fw1);
			bw1.write("");
			FileWriter fw2=new FileWriter(scoreDoubleRecord,false);
			BufferedWriter bw2=new BufferedWriter(fw2);
			bw2.write("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//this.pack();
		this.setResizable(false);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				c.disconnect();
				System.exit(0);
			}
		});
	}
	
	class ActionMonitor implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource()==control.buttonConnect)//连接到服务器
			{
				connect();
			}
			else if (e.getSource()==control.buttonJoin)//邀请对方加入游戏
			{
				String select=userList.userList.getSelectedItem();//在用户列表区选择队友
				if (select==null) {
					message.messageArea.append("请选择一个对手"+"\n");
					return;
				}
				else if (!select.endsWith("ready"))
				{
					message.messageArea.append("只能选择状态为ready的玩家"+"\n");
					return;
				}
				else if (select.startsWith(myName))
				{
					message.messageArea.append("不能选择自己作为对手"+"\n");
					return;
				}
				int index=select.lastIndexOf(":");
				String name=select.substring(0,index);//获取队友的名字
				join(name);
				
			}
			
			else if (e.getSource()==chat.buttonSend)
			{
				//发送textChat中的内容
				String msg=chat.textChat.getText();
				try {
					c.chat(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//将textChat中的内容添加到chatHistory中
				chat.chatHistory.append("Me:"+msg+"\n");
				//清空textChat
				chat.textChat.setText("");
				
			}
			
			else if (e.getSource()==chat.buttonClear)
			{
				//清空textChat
				
				chat.textChat.setText("");
				
			}
			
			else if (e.getSource()==control.buttonExit)
			{
				if (isConnected)
					c.disconnect();
				System.exit(0);
			}
			
		}
	}
	
	//以下为客户端主动发出的消息
	public void connect()//连接服务器，调用c中的connect函数
	{
		c=new Communication(this);
		String IP=control.inputIP.getText();
		c.connect(IP, GoldMinerServer.TCP_PORT);//调用c中connecnt方法，连接到一个指定IP和PORT的服务器
		
		//更新界面
		message.messageArea.append("已连接"+"\n");
		isConnected=true;
		control.buttonExit.setEnabled(true);
		control.buttonConnect.setEnabled(false);
		control.inputIP.setFocusable(false);
		control.buttonJoin.setEnabled(true);
	}
	
	public void join(String opponentName)//根据队友的名字发出邀请
	{
		c.join(opponentName);//将邀请交给communication处理
	}

	
	//本次游戏结束，寻找新的伙伴
	public void finishGame()
	{
		play.willBegin=false;
		play.cal.show(play, "begin");
		play.setVisible(false);
		opName=null;
		control.buttonJoin.setEnabled(true);
		control.buttonExit.setEnabled(true);
	}
	

	
	
}

class MyComparator implements Comparator<SingleRecord>{

	@Override
	public int compare(SingleRecord s1, SingleRecord s2) {
		// TODO Auto-generated method stub
		 if(s1.getScore()-s2.getScore()!=0)
		  {
		       return s2.getScore()-s1.getScore();
		  }
		  else
		  {
		       return  s1.getDate().getTime()<s2.getDate().getTime()?-1:1;
		  }
	}
}