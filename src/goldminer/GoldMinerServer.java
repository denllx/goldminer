package goldminer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;


import java.net.*;
import java.util.*;


public class GoldMinerServer extends JFrame implements ActionListener{

	JLabel lStatus=new JLabel("��ǰ��������",JLabel.LEFT);
	JTextArea taMessage=new JTextArea("",22,50);//��ſͻ�����Ϣ
	JButton btnServerClose=new JButton("�رշ�����");
	ServerSocket ss=null;
	public static final int TCP_PORT=4811;
	static int clientNum=0;//�����ڷ������Ͽͻ�������
	static int clientNameNum=0;//�ͻ����ֵ�����
	ArrayList<Client> clients=new ArrayList<Client>();//����û���Ϣ������
	ArrayList<Socket> sockets=new ArrayList<Socket>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GoldMinerServer server=new GoldMinerServer();
		server.startServer();
	}
	
	public GoldMinerServer()
	{
		super("�ƽ��������");//����	
		btnServerClose.addActionListener(this);//Ϊ�˳���ť��Ӽ�����
		//��װ����
		this.add(lStatus,BorderLayout.NORTH);
		this.add(taMessage, BorderLayout.CENTER);
		this.add(btnServerClose, BorderLayout.SOUTH);
		this.setLocation(400, 100);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);//���ɸ���
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
				Client c=new Client("Player"+clientNameNum,s);//�ͻ���������"player1"�ĸ�ʽ
				clients.add(c);//���¿ͻ�����ͻ��б�
				lStatus.setText("��������"+clientNum);
				//��ȡ�ͻ���IP��ַ
				String msg=s.getInetAddress().getHostAddress()+"Player"+clientNameNum+"\n";
				taMessage.append(msg);
				tellName(c);//�����ָ�֪���ͻ�
				addAllUserToMe(c);//�������Ͽͻ���Ϣ֪ͨ�¿ͻ�
				addMeToAllUser(c);//���¿ͻ���Ϣ֪ͨ�����Ͽͻ�
				new ClientThread(c).start();//Ϊÿ���ͻ�����һ����Ϣ�����߳�

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�����������ָ�֪�ͻ�
	public void tellName(Client c)
	{
		DataOutputStream dos=null;
		try {
			dos=new DataOutputStream(c.s.getOutputStream());//��ͻ��������Ϣ
			dos.writeUTF(Command.TELLNAME+":"+c.name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//���������������еĿͻ���֪�¿ͻ�
	public void addAllUserToMe(Client c)//c�����û�
	{
		DataOutputStream dos=null;
		for (int i=0;i<clients.size();i++)//�������пͻ��б��е�ÿ����
		{
			if (clients.get(i)!=c)//�Ͽͻ�
			{
				try {
					dos=new DataOutputStream(c.s.getOutputStream());//���¿ͻ������Ϣ
					dos.writeUTF(Command.ADD+":"+clients.get(i).name+":"+clients.get(i).state);//�Ͽͻ�����Ϣ
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//���Ͽͻ������Ϣ
				
			}
		}
	}
	
	//���������¿ͻ���֪���пͻ�
	public void addMeToAllUser(Client c)//c���¿ͻ�
	{
		DataOutputStream dos=null;
		for (int i=0;i<clients.size();i++)
		{
			if (clients.get(i)!=c)
			{
				try {
					dos=new DataOutputStream(clients.get(i).s.getOutputStream());//���Ͽͻ������Ϣ
					dos.writeUTF(Command.ADD+":"+c.name+":"+"ready");//����¿ͻ�����Ϣ
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	//��¼�����ڷ������ϵĿͻ�����Ϣ
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
	
	//���Ͻ������Կͻ�����Ϣ
	class ClientThread extends Thread{
		private Client c;//��������˭����Ϣ
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
					if (words[0].equals(Command.JOIN))//�ͻ�������һ�����
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
					
					else if (words[0].equals(Command.REFUSE))//�ͻ��ܾ���һ��ҵ�����
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
					
					else if (words[0].equals(Command.AGREE))//�ͻ�ͬ����һ��ҵ�����
					{
						//�ı�c��״̬��opname
						c.state="playing";
						String opponentName=words[1];
						
						//�ı�opponent��״̬
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
						
						//�����Ͽͻ�������б�
						for (int i=0;i<clients.size();i++)
						{
							dos=new DataOutputStream(clients.get(i).s.getOutputStream());
							dos.writeUTF(Command.CHANGE+":"+c.name+":playing");
							dos.writeUTF(Command.CHANGE+":"+c.opponent.name+":playing");
						}
						
						
						//���·������б�
						taMessage.append(c.name+" playing\n");
						taMessage.append(c.opponent.name+" playing\n");
						
						//���������֪ͨ����
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.LR+":left:"+c.opponent.name);
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.LR+":right:"+c.name);//���շ�Ϊ�ұ�
						
						//���·������б�
						taMessage.append(c.name+" left\n");
						taMessage.append(c.opponent.name+" right\n");
						System.out.println("server:agree");
	
					}
					
					//һ�����˿�ʼ��ť
					else if (words[0].equals(Command.BEGIN))
					{
						//��֪������Ϸ��ʼ
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.OPBEGIN+":");
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.OPBEGIN+":");
						
					}
					
					else if (words[0].equals(Command.GO))
					{
						//֪ͨ�Է����ԷŹ�����
						System.out.println("server1:"+System.nanoTime());
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(msg);
						//֪ͨ�ҿ��ԷŹ�����
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
					
					//Э����ˮ
					else if (words[0].equals(Command.CHAT))
					{
						
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(msg);//chat:˵�Ļ�
						taMessage.append(c.name+" "+msg+"\n");
						
					}
					
					//����
					else if (words[0].equals(Command.CHAT))//chat:˵��
					{
						String realmsg=words[1];//˵������
						String name=c.name;
						dos=new DataOutputStream(c.opponent.s.getOutputStream());//֪ͨ����
						dos.writeUTF(Command.CHAT+":"+name+":"+realmsg);
						
					}
					
					//��ˮ
					else if (words[0].equals(Command.BUY))
					{
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.BUY+":");
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.BUY+":");
					}
					
					//��һ��
					else if (words[0].equals(Command.NEXTLEVEL))
					{
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.NEXTLEVEL+":");
					}
					
					//������Ϸ
					else if (words[0].equals(Command.GIVEUP))
					{
						//�޸�״̬
						for (int i=0;i<clients.size();i++)
						{
							dos=new DataOutputStream(clients.get(i).s.getOutputStream());
							dos.writeUTF(Command.CHANGE+":"+c.name+":ready");
							dos.writeUTF(Command.CHANGE+":"+c.opponent.name+":ready");
						}
						c.state="ready";
						c.opponent.state="ready";
						//��֪��Ϸ����
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.GIVEUP+":");
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.GIVEUP+":");
					}
					
					//����
					else if (words[0].equals(Command.AGAIN))
					{
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.AGAIN+":");
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.AGAIN+":");
						taMessage.append(c.name+" plays again"+"\n");
						taMessage.append(c.opponent+" plays again"+"\n");
					}
					
					//�û��˳�������
					else if (words[0].equals(Command.QUIT))
					{
						//���û��б���ɾȥ
						for (int i=0;i<clients.size();i++)
						{
							if (clients.get(i)!=c)
							{
								dos=new DataOutputStream(clients.get(i).s.getOutputStream());
								dos.writeUTF(Command.DELETE+":"+c.name);
							}
						}
						clients.remove(c);
						
						//���·���������
						taMessage.append(c.name+" quit\n");
						clientNum--;
						lStatus.setText("������"+clientNum);
						return;
					}
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//�������Կͻ��˵���Ϣ
			}
		}
	}

}
