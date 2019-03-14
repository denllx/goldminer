package goldminer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;


import java.net.*;
import java.util.*;


public class GoldMinerServer extends JFrame implements ActionListener{

	JLabel lStatus=new JLabel("当前连接数：",JLabel.LEFT);
	JTextArea taMessage=new JTextArea("",22,50);//存放客户端信息
	JButton btnServerClose=new JButton("关闭服务器");
	ServerSocket ss=null;
	public static final int TCP_PORT=4811;
	static int clientNum=0;//连接在服务器上客户的数量
	static int clientNameNum=0;//客户名字的数量
	ArrayList<Client> clients=new ArrayList<Client>();//存放用户信息的链表
	ArrayList<Socket> sockets=new ArrayList<Socket>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GoldMinerServer server=new GoldMinerServer();
		server.startServer();
	}
	
	public GoldMinerServer()
	{
		super("黄金矿工联机版");//标题	
		btnServerClose.addActionListener(this);//为退出按钮添加监听器
		//组装界面
		this.add(lStatus,BorderLayout.NORTH);
		this.add(taMessage, BorderLayout.CENTER);
		this.add(btnServerClose, BorderLayout.SOUTH);
		this.setLocation(400, 100);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);//不可更改
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==btnServerClose)
		{
			try {
				ss.close();
				for (int i=0;i<sockets.size();i++)
					sockets.get(i).close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.exit(0);
		}
	}
	
	public void startServer()
	{
		try {
			ss=new ServerSocket(TCP_PORT);
			while(true)
			{
				Socket s=ss.accept();
				sockets.add(s);
				clientNum++;
				clientNameNum++;
				Client c=new Client("Player"+clientNameNum,s);//客户的名字是"player1"的格式
				clients.add(c);//将新客户加入客户列表
				lStatus.setText("连接数："+clientNum);
				//获取客户的IP地址
				String msg=s.getInetAddress().getHostAddress()+"Player"+clientNameNum+"\n";
				taMessage.append(msg);
				tellName(c);//将名字告知给客户
				addAllUserToMe(c);//将所有老客户信息通知新客户
				addMeToAllUser(c);//将新客户信息通知所有老客户
				new ClientThread(c).start();//为每个客户启动一个消息处理线程

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//服务器将名字告知客户
	public void tellName(Client c)
	{
		DataOutputStream dos=null;
		try {
			dos=new DataOutputStream(c.s.getOutputStream());//向客户端输出信息
			dos.writeUTF(Command.TELLNAME+":"+c.name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//服务器将所有已有的客户告知新客户
	public void addAllUserToMe(Client c)//c是新用户
	{
		DataOutputStream dos=null;
		for (int i=0;i<clients.size();i++)//遍历已有客户列表中的每个人
		{
			if (clients.get(i)!=c)//老客户
			{
				try {
					dos=new DataOutputStream(c.s.getOutputStream());//向新客户输出信息
					dos.writeUTF(Command.ADD+":"+clients.get(i).name+":"+clients.get(i).state);//老客户的信息
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//向老客户输出信息
				
			}
		}
	}
	
	//服务器将新客户告知已有客户
	public void addMeToAllUser(Client c)//c是新客户
	{
		DataOutputStream dos=null;
		for (int i=0;i<clients.size();i++)
		{
			if (clients.get(i)!=c)
			{
				try {
					dos=new DataOutputStream(clients.get(i).s.getOutputStream());//向老客户输出信息
					dos.writeUTF(Command.ADD+":"+c.name+":"+"ready");//输出新客户的信息
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	//记录连接在服务器上的客户的信息
	class Client{
		String name;
		Socket s;
		String state;
		Client opponent;
		public Client(String name_,Socket s_)
		{
			this.name=name_;
			this.s=s_;
			this.state="ready";
			this.opponent=null;
		}
	}
	
	//不断接受来自客户端消息
	class ClientThread extends Thread{
		private Client c;//处理来自谁的消息
		private DataInputStream dis;
		private DataOutputStream dos;
		public ClientThread(Client c_)
		{
			this.c=c_;
		}
		
		public void run()
		{
			while(true)
			{
				try {
					dis=new DataInputStream(c.s.getInputStream());
					String msg=dis.readUTF();
					String[] words=msg.split(":");
					if (words[0].equals(Command.JOIN))//客户端邀请一名玩家
					{
						String opponentName=words[1];
						for (int i=0;i<clients.size();i++)
						{
							if (clients.get(i).name.equals(opponentName))
							{
								dos=new DataOutputStream(clients.get(i).s.getOutputStream());
								dos.writeUTF(Command.JOIN+":"+c.name);
							}
						}
					}
					
					else if (words[0].equals(Command.REFUSE))//客户拒绝另一玩家的邀请
					{
						String opponentName=words[1];
						for (int i=0;i<clients.size();i++)
						{
							if (clients.get(i).name.equals(opponentName))
							{
								dos=new DataOutputStream(clients.get(i).s.getOutputStream());
								dos.writeUTF(Command.REFUSE+":"+c.name);
								break;
							}
						}
					}
					
					else if (words[0].equals(Command.AGREE))//客户同意另一玩家的邀请
					{
						//改变c的状态、opname
						c.state="playing";
						String opponentName=words[1];
						
						//改变opponent的状态
						for (int i=0;i<clients.size();i++)
						{
							if (clients.get(i).name.equals(opponentName))
							{
								clients.get(i).state="playing";
								clients.get(i).opponent=c;
								c.opponent=clients.get(i);
								break;
							}
						}
						
						//更改老客户的玩家列表
						for (int i=0;i<clients.size();i++)
						{
							dos=new DataOutputStream(clients.get(i).s.getOutputStream());
							dos.writeUTF(Command.CHANGE+":"+c.name+":playing");
							dos.writeUTF(Command.CHANGE+":"+c.opponent.name+":playing");
						}
						
						
						//更新服务器列表
						taMessage.append(c.name+" playing\n");
						taMessage.append(c.opponent.name+" playing\n");
						
						//向两个玩家通知左右
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.LR+":left:"+c.opponent.name);
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.LR+":right:"+c.name);//接收方为右边
						
						//更新服务器列表
						taMessage.append(c.name+" left\n");
						taMessage.append(c.opponent.name+" right\n");
						System.out.println("server:agree");
	
					}
					
					//一方点了开始按钮
					else if (words[0].equals(Command.BEGIN))
					{
						//告知两方游戏开始
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.OPBEGIN+":");
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.OPBEGIN+":");
						
					}
					
					else if (words[0].equals(Command.GO))
					{
						//通知对方可以放钩子了
						System.out.println("server1:"+System.nanoTime());
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(msg);
						//通知我可以放钩子了
						System.out.println("server2:"+System.nanoTime());
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(msg);
					}
					
					else if (words[0].equals(Command.PAUSE))
					{
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(msg);
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(msg);
					}
					
					else if (words[0].equals(Command.GOON))
					{
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(msg);
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(msg);
					}
					
					//协商买水
					else if (words[0].equals(Command.CHAT))
					{
						
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(msg);//chat:说的话
						taMessage.append(c.name+" "+msg+"\n");
						
					}
					
					//聊天
					else if (words[0].equals(Command.CHAT))//chat:说话
					{
						String realmsg=words[1];//说话内容
						String name=c.name;
						dos=new DataOutputStream(c.opponent.s.getOutputStream());//通知队友
						dos.writeUTF(Command.CHAT+":"+name+":"+realmsg);
						
					}
					
					//买水
					else if (words[0].equals(Command.BUY))
					{
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.BUY+":");
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.BUY+":");
					}
					
					//下一关
					else if (words[0].equals(Command.NEXTLEVEL))
					{
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.NEXTLEVEL+":");
					}
					
					//放弃游戏
					else if (words[0].equals(Command.GIVEUP))
					{
						//修改状态
						for (int i=0;i<clients.size();i++)
						{
							dos=new DataOutputStream(clients.get(i).s.getOutputStream());
							dos.writeUTF(Command.CHANGE+":"+c.name+":ready");
							dos.writeUTF(Command.CHANGE+":"+c.opponent.name+":ready");
						}
						c.state="ready";
						c.opponent.state="ready";
						//告知游戏结束
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.GIVEUP+":");
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.GIVEUP+":");
					}
					
					//重玩
					else if (words[0].equals(Command.AGAIN))
					{
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.AGAIN+":");
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.AGAIN+":");
						taMessage.append(c.name+" plays again"+"\n");
						taMessage.append(c.opponent+" plays again"+"\n");
					}
					
					//用户退出服务器
					else if (words[0].equals(Command.QUIT))
					{
						//从用户列表上删去
						for (int i=0;i<clients.size();i++)
						{
							if (clients.get(i)!=c)
							{
								dos=new DataOutputStream(clients.get(i).s.getOutputStream());
								dos.writeUTF(Command.DELETE+":"+c.name);
							}
						}
						clients.remove(c);
						
						//更新服务器界面
						taMessage.append(c.name+" quit\n");
						clientNum--;
						lStatus.setText("连接数"+clientNum);
						return;
					}
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//接受来自客户端的消息
			}
		}
	}

}
