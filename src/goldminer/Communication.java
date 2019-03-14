package goldminer;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.io.*;


//用于处理客户端接受的消息
public class Communication {

	GoldMinerClient gc;//负责处理谁的消息
	Socket s;
	private DataOutputStream dos;
	private DataInputStream dis;
	public SoundPlayer sound=new SoundPlayer();
	
	public Communication(GoldMinerClient gc_)
	{
		gc=gc_;
	}
	
	//连接到服务器
	public void connect(String IP,int port)//为负责的客户连接指定端口和IP的服务器
	{
		try {
			s=new Socket(IP,port);//创建一个和服务器此端口通信的本地socket
			//创建接受消息的线程
			new ReceiveThread(s).start();
			dis=new DataInputStream(s.getInputStream());
			dos=new DataOutputStream(s.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//处理来自客户端主动发出的消息
	//根据队友的名字发出邀请
	public void join(String OpponentName)
	{
		try {
			dos.writeUTF(Command.JOIN+":"+OpponentName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//希望另一方开始
	public void wantBegin()
	{
		try {
			//向服务器告知自己点了开始按钮
			dos.writeUTF(Command.BEGIN+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//告知服务器我方放钩子
	public void go()
	{
		try {
			dos.writeUTF(Command.GO+":"+gc.play.isLeft);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//告知对方我要暂停
	public void pause()
	{
		try {
			dos.writeUTF(Command.PAUSE+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//告知对方我要继续
	public void goon()
	{
		try {
			dos.writeUTF(Command.GOON+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//协商是否买升力水
	public void chat(String chatWords) throws IOException
	{
		dos.writeUTF(Command.CHAT+":"+chatWords);
	}
	
	//告知对方自己买了升力水
	public void buy()
	{
		try {
			dos.writeUTF(Command.BUY+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//告知对方自己进入下一关
	public void nextLevel()
	{
		try {
			dos.writeUTF(Command.NEXTLEVEL+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//告知对方自己退出游戏
	public void giveup()
	{
		try {
			dos.writeUTF(Command.GIVEUP+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//通知对方准备重玩
	public void again()
	{
		try {
			dos.writeUTF(Command.AGAIN+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//退出服务器
	public void disconnect()
	{
		try {
			dos.writeUTF(Command.QUIT+":");
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//处理来自服务器的消息
	class ReceiveThread extends Thread{
		Socket s;//传输消息的服务器socket
		private DataInputStream dis;
		private DataOutputStream dos;
		String msg;//读入的消息
		public ReceiveThread(Socket s_)//接受来自指定socket的消息
		{
			this.s=s_;
		}
		
		public void run()
		{
			while(true)
			{
				try {
					dis=new DataInputStream(s.getInputStream());
					dos=new DataOutputStream(s.getOutputStream());
					msg=dis.readUTF();
					String[] words=msg.split(":");
					
					//如果服务器告知了用户的名字
					if (words[0].equals(Command.TELLNAME))
					{
						gc.myName=words[1];
						gc.userList.userList.add(gc.myName+":ready");
						gc.message.messageArea.append("My name:"+gc.myName+"\n");
					}
					
					//如果服务器告知添加用户
					else if (words[0].equals(Command.ADD))
					{
						gc.userList.userList.add(words[1]+":"+words[2]);//名字+状态
						gc.message.messageArea.append(words[1]+":"+words[2]+"\n");
					}
					
					//如果服务器告知用户有人邀请他
					else if (words[0].equals(Command.JOIN))
					{
						String name=words[1];//谁邀请他
						TimeDialog d=new TimeDialog();
						//在gc(client)所在窗口上弹出一个dialog,倒计时100秒
						int select=d.showDialog(gc, name+"邀请您挖矿，您是否同意？", 100);
						if (select==0)//接受
							dos.writeUTF(Command.AGREE+":"+name);//同意对方的邀请
						else
							dos.writeUTF(Command.REFUSE+":"+name);//拒绝对方的邀请
					}
					
					
				
					
					//如果被拒绝了
					else if (words[0].equals(Command.REFUSE))
					{
						String name=words[1];
						JOptionPane.showMessageDialog(gc, name+"拒绝了您的邀请！");
					}
					
					//更改用户信息
					else if (words[0].equals(Command.CHANGE))
					{
						//更改用户列表
						String name=words[1];
						String state=words[2];
						for (int i=0;i<gc.userList.userList.getItemCount();i++)
						{
							if (gc.userList.userList.getItem(i).startsWith(name))
								gc.userList.userList.replaceItem(name+":"+state,i);
						}
						gc.message.messageArea.append(name+" "+state+"\n");
					}
					
					//双方同意组队，接受左右分配结果
					else if (words[0].equals(Command.LR))
					{
						gc.opName=words[2];
						gc.play.willBegin=true;
						gc.control.buttonJoin.setEnabled(false);
						gc.control.buttonConnect.setEnabled(false);
						gc.control.buttonExit.setEnabled(false);
						
						//在panelplay中显示开始界面
						gc.play.setVisible(true);
						gc.play.isBeginning=true;
						String lr=words[1];//left  right
						if (lr.equals("left"))
							gc.play.isLeft=true;//自己是左边的人

						else
							gc.play.isLeft=false;//自己是右边的人
						gc.play.panelMining.reset();
						//重绘界面让开始按钮显示
						gc.play.panelBegin.repaint();
					}
					
					//对方按下开始按钮，自己自动启动游戏
					else if (words[0].equals(Command.OPBEGIN))
					{
						gc.play.cal.next(gc.play);
						//System.out.println("begin:"+System.currentTimeMillis());
						gc.play.panelGoal.setLevel(1);
						gc.play.panelGoal.startTimer();
						//gc.play.repaint();
					}
					
					else if (words[0].equals(Command.GO))
					{
						boolean isLeft=(words[1].equals("true"))?true:false;
						//如果isLeft和自己相同，就是自己放钩子，反之对方放钩子
						if (isLeft==gc.play.isLeft)
							gc.play.panelMining.isSearching1=true;
						else
							gc.play.panelMining.isSearching2=true;
						System.out.println("isleft:"+isLeft+" time:"+System.nanoTime());
						System.out.println("go:"+System.currentTimeMillis());
						//重绘mining界面
						gc.play.panelMining.repaint();
						//放音乐
						sound.loadSound("sound/catch.wav");
						sound.playSound();
					}
					
					else if (words[0].equals(Command.PAUSE))
					{
						gc.play.panelMining.pauseGame();
					}
					
					else if (words[0].equals(Command.GOON))
					{
						gc.play.panelMining.continueGame();
					}
					
					//对方协商买水
					else if (words[0].equals(Command.CHAT))//chat:说话内容
					{
						//添加聊天记录
						String realmsg=msg.substring(Command.CHAT.length()
								+1, msg.length());
						gc.chat.chatHistory.append(gc.myName+":"+realmsg+"\n");//名字:消息
					}
					
					//对方买了水
					else if (words[0].equals(Command.BUY))
					{
						//更改数据
						gc.play.score1-=gc.play.panelBuy.waterValue/2;
						gc.play.score2-=gc.play.panelBuy.waterValue/2;
						gc.play.score-=gc.play.panelBuy.waterValue/2;
						gc.play.speedTime=1.5;
						//进入下一关
						gc.play.panelBuy.nextLevel();
					}
					
					//对方点击进入下一挂
					else if (words[0].equals(Command.NEXTLEVEL))
					{
						gc.play.panelBuy.nextLevel();
					}
					
					//对方放弃了游戏
					else if (words[0].equals(Command.GIVEUP))
					{
						gc.finishGame();
					}
					
					//对方重玩
					else if (words[0].equals(Command.AGAIN))
					{
						gc.play.again();
					}
					
					//删去用户
					else if (words[0].equals(Command.DELETE))
					{
						if (gc.opName.equals(words[1]))
						{
							JOptionPane.showMessageDialog(gc, "您的伙伴退出了游戏！");
							//结束游戏
							gc.finishGame();
						}
						for (int i=0;i<gc.userList.userList.getItemCount();i++)
						{
							if (gc.userList.userList.getItem(i).startsWith(words[1]))
							{
								gc.userList.userList.remove(i);
							}
			
						}
						gc.message.messageArea.append(words[1]+" disconnected\n");
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
}
