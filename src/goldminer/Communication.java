package goldminer;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.io.*;


//���ڴ���ͻ��˽��ܵ���Ϣ
public class Communication {

	GoldMinerClient gc;//������˭����Ϣ
	Socket s;
	private DataOutputStream dos;
	private DataInputStream dis;
	public SoundPlayer sound=new SoundPlayer();
	
	public Communication(GoldMinerClient gc_)
	{
		gc=gc_;
	}
	
	//���ӵ�������
	public void connect(String IP,int port)//Ϊ����Ŀͻ�����ָ���˿ں�IP�ķ�����
	{
		try {
			s=new Socket(IP,port);//����һ���ͷ������˶˿�ͨ�ŵı���socket
			//����������Ϣ���߳�
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
	//�������Կͻ���������������Ϣ
	//���ݶ��ѵ����ַ�������
	public void join(String OpponentName)
	{
		try {
			dos.writeUTF(Command.JOIN+":"+OpponentName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//ϣ����һ����ʼ
	public void wantBegin()
	{
		try {
			//���������֪�Լ����˿�ʼ��ť
			dos.writeUTF(Command.BEGIN+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��֪�������ҷ��Ź���
	public void go()
	{
		try {
			dos.writeUTF(Command.GO+":"+gc.play.isLeft);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��֪�Է���Ҫ��ͣ
	public void pause()
	{
		try {
			dos.writeUTF(Command.PAUSE+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��֪�Է���Ҫ����
	public void goon()
	{
		try {
			dos.writeUTF(Command.GOON+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Э���Ƿ�������ˮ
	public void chat(String chatWords) throws IOException
	{
		dos.writeUTF(Command.CHAT+":"+chatWords);
	}
	
	//��֪�Է��Լ���������ˮ
	public void buy()
	{
		try {
			dos.writeUTF(Command.BUY+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��֪�Է��Լ�������һ��
	public void nextLevel()
	{
		try {
			dos.writeUTF(Command.NEXTLEVEL+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��֪�Է��Լ��˳���Ϸ
	public void giveup()
	{
		try {
			dos.writeUTF(Command.GIVEUP+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//֪ͨ�Է�׼������
	public void again()
	{
		try {
			dos.writeUTF(Command.AGAIN+":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�˳�������
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
	//�������Է���������Ϣ
	class ReceiveThread extends Thread{
		Socket s;//������Ϣ�ķ�����socket
		private DataInputStream dis;
		private DataOutputStream dos;
		String msg;//�������Ϣ
		public ReceiveThread(Socket s_)//��������ָ��socket����Ϣ
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
					
					//�����������֪���û�������
					if (words[0].equals(Command.TELLNAME))
					{
						gc.myName=words[1];
						gc.userList.userList.add(gc.myName+":ready");
						gc.message.messageArea.append("My name:"+gc.myName+"\n");
					}
					
					//�����������֪����û�
					else if (words[0].equals(Command.ADD))
					{
						gc.userList.userList.add(words[1]+":"+words[2]);//����+״̬
						gc.message.messageArea.append(words[1]+":"+words[2]+"\n");
					}
					
					//�����������֪�û�����������
					else if (words[0].equals(Command.JOIN))
					{
						String name=words[1];//˭������
						TimeDialog d=new TimeDialog();
						//��gc(client)���ڴ����ϵ���һ��dialog,����ʱ100��
						int select=d.showDialog(gc, name+"�������ڿ����Ƿ�ͬ�⣿", 100);
						if (select==0)//����
							dos.writeUTF(Command.AGREE+":"+name);//ͬ��Է�������
						else
							dos.writeUTF(Command.REFUSE+":"+name);//�ܾ��Է�������
					}
					
					
				
					
					//������ܾ���
					else if (words[0].equals(Command.REFUSE))
					{
						String name=words[1];
						JOptionPane.showMessageDialog(gc, name+"�ܾ����������룡");
					}
					
					//�����û���Ϣ
					else if (words[0].equals(Command.CHANGE))
					{
						//�����û��б�
						String name=words[1];
						String state=words[2];
						for (int i=0;i<gc.userList.userList.getItemCount();i++)
						{
							if (gc.userList.userList.getItem(i).startsWith(name))
								gc.userList.userList.replaceItem(name+":"+state,i);
						}
						gc.message.messageArea.append(name+" "+state+"\n");
					}
					
					//˫��ͬ����ӣ��������ҷ�����
					else if (words[0].equals(Command.LR))
					{
						gc.opName=words[2];
						gc.play.willBegin=true;
						gc.control.buttonJoin.setEnabled(false);
						gc.control.buttonConnect.setEnabled(false);
						gc.control.buttonExit.setEnabled(false);
						
						//��panelplay����ʾ��ʼ����
						gc.play.setVisible(true);
						gc.play.isBeginning=true;
						String lr=words[1];//left  right
						if (lr.equals("left"))
							gc.play.isLeft=true;//�Լ�����ߵ���

						else
							gc.play.isLeft=false;//�Լ����ұߵ���
						gc.play.panelMining.reset();
						//�ػ�����ÿ�ʼ��ť��ʾ
						gc.play.panelBegin.repaint();
					}
					
					//�Է����¿�ʼ��ť���Լ��Զ�������Ϸ
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
						//���isLeft���Լ���ͬ�������Լ��Ź��ӣ���֮�Է��Ź���
						if (isLeft==gc.play.isLeft)
							gc.play.panelMining.isSearching1=true;
						else
							gc.play.panelMining.isSearching2=true;
						System.out.println("isleft:"+isLeft+" time:"+System.nanoTime());
						System.out.println("go:"+System.currentTimeMillis());
						//�ػ�mining����
						gc.play.panelMining.repaint();
						//������
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
					
					//�Է�Э����ˮ
					else if (words[0].equals(Command.CHAT))//chat:˵������
					{
						//��������¼
						String realmsg=msg.substring(Command.CHAT.length()
								+1, msg.length());
						gc.chat.chatHistory.append(gc.myName+":"+realmsg+"\n");//����:��Ϣ
					}
					
					//�Է�����ˮ
					else if (words[0].equals(Command.BUY))
					{
						//��������
						gc.play.score1-=gc.play.panelBuy.waterValue/2;
						gc.play.score2-=gc.play.panelBuy.waterValue/2;
						gc.play.score-=gc.play.panelBuy.waterValue/2;
						gc.play.speedTime=1.5;
						//������һ��
						gc.play.panelBuy.nextLevel();
					}
					
					//�Է����������һ��
					else if (words[0].equals(Command.NEXTLEVEL))
					{
						gc.play.panelBuy.nextLevel();
					}
					
					//�Է���������Ϸ
					else if (words[0].equals(Command.GIVEUP))
					{
						gc.finishGame();
					}
					
					//�Է�����
					else if (words[0].equals(Command.AGAIN))
					{
						gc.play.again();
					}
					
					//ɾȥ�û�
					else if (words[0].equals(Command.DELETE))
					{
						if (gc.opName.equals(words[1]))
						{
							JOptionPane.showMessageDialog(gc, "���Ļ���˳�����Ϸ��");
							//������Ϸ
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
