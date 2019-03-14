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
	String opName;//��������
	public boolean isConnected=false;//�Ƿ��Ѿ����ӵ�������
	Communication c=null;//˭�������ҵ���Ϣ
	public File scoreSingleRecord=new File("single.txt");//���˳ɼ���¼
	public File scoreDoubleRecord=new File("double.txt");//˫�˳ɼ���¼
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
		super("�ƽ��������");//����
		
		//ʵ�廯���
		userList=new PanelUserList();
		message=new PanelMessage();
		control=new PanelControl();
		play=new PanelPlay(this);
		chat=new PanelChat();
		
		//��Ӽ�����
		ActionMonitor monitor=new ActionMonitor();
		control.buttonConnect.addActionListener(monitor);
		control.buttonExit.addActionListener(monitor);
		control.buttonJoin.addActionListener(monitor);
		chat.buttonClear.addActionListener(monitor);
		chat.buttonSend.addActionListener(monitor);
		//ȡ������
		control.buttonConnect.setFocusable(false);
		control.buttonExit.setFocusable(false);
		control.buttonJoin.setFocusable(false);
		userList.userList.setFocusable(false);
		message.messageArea.setFocusable(false);
		
		this.setFocusable(true);
		
		//���ð�ť��ʼװ��
		control.buttonConnect.setEnabled(true);
		control.buttonExit.setEnabled(true);
		control.buttonJoin.setEnabled(false);
		
		//��ʼ���Ƚ���
		MyComparator myComp=new MyComparator();
		Collections.sort(singleList, myComp);
		Collections.sort(doubleList,myComp);
		
		//��װ����
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
		
		//����ļ�
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
			if (e.getSource()==control.buttonConnect)//���ӵ�������
			{
				connect();
			}
			else if (e.getSource()==control.buttonJoin)//����Է�������Ϸ
			{
				String select=userList.userList.getSelectedItem();//���û��б���ѡ�����
				if (select==null) {
					message.messageArea.append("��ѡ��һ������"+"\n");
					return;
				}
				else if (!select.endsWith("ready"))
				{
					message.messageArea.append("ֻ��ѡ��״̬Ϊready�����"+"\n");
					return;
				}
				else if (select.startsWith(myName))
				{
					message.messageArea.append("����ѡ���Լ���Ϊ����"+"\n");
					return;
				}
				int index=select.lastIndexOf(":");
				String name=select.substring(0,index);//��ȡ���ѵ�����
				join(name);
				
			}
			
			else if (e.getSource()==chat.buttonSend)
			{
				//����textChat�е�����
				String msg=chat.textChat.getText();
				try {
					c.chat(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//��textChat�е�������ӵ�chatHistory��
				chat.chatHistory.append("Me:"+msg+"\n");
				//���textChat
				chat.textChat.setText("");
				
			}
			
			else if (e.getSource()==chat.buttonClear)
			{
				//���textChat
				
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
	
	//����Ϊ�ͻ���������������Ϣ
	public void connect()//���ӷ�����������c�е�connect����
	{
		c=new Communication(this);
		String IP=control.inputIP.getText();
		c.connect(IP, GoldMinerServer.TCP_PORT);//����c��connecnt���������ӵ�һ��ָ��IP��PORT�ķ�����
		
		//���½���
		message.messageArea.append("������"+"\n");
		isConnected=true;
		control.buttonExit.setEnabled(true);
		control.buttonConnect.setEnabled(false);
		control.inputIP.setFocusable(false);
		control.buttonJoin.setEnabled(true);
	}
	
	public void join(String opponentName)//���ݶ��ѵ����ַ�������
	{
		c.join(opponentName);//�����뽻��communication����
	}

	
	//������Ϸ������Ѱ���µĻ��
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