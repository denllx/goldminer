package goldminer;
import java.util.concurrent.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//��ʾ����Ƿ��������Ķ�ʱ�Ի���
public class TimeDialog {
	private String message=null;
	private int seconds=0;//��˼��ʱ��
	private JLabel label=new JLabel();
	private JButton confirm;//��������
	private JButton cancel;//����������
	private JDialog dialog=null;
	int result=-5;//����Ϊ0��,�ܾ�Ϊ1����ʱΪ-5
	
	public int showDialog(JFrame father,String message_,int sec)
	{
		this.message=message_;//��������������Ƿ�ͬ�⣿
		seconds=sec;
		label.setText(message_);
		label.setBounds(80,6,200,20);
		ScheduledExecutorService s=Executors.newSingleThreadScheduledExecutor();
		confirm=new JButton("����");
		confirm.setBounds(100,40,60,20);
		
		//������½��ܼ���result=0,�Ի�����ʧ
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				result=0;
				TimeDialog.this.dialog.dispose();
			}
		});
		cancel=new JButton("�ܾ�");
		cancel.setBounds(190,40,60,20);
		
		//������¡��ܾ�������result=1,�Ի�����ʧ
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				result=1;
				TimeDialog.this.dialog.dispose();
			}
		});
		
		//���ƶԻ���
		dialog=new JDialog(father,true);//�Ի�����ģ̬�򣬸�������father
		dialog.setTitle("��ʾ�������ڽ���"+seconds+"���ر�");
		dialog.setLayout(null);
		dialog.add(label);//��������������Ƿ�ͬ�⣿
		dialog.add(confirm);
		dialog.add(cancel);
		
		//runnable����ָ����ʱ�����ظ�ִ��
		s.scheduleAtFixedRate(new Runnable() {
			public void run()
			{
				TimeDialog.this.seconds--;
				if (TimeDialog.this.seconds==0)
					TimeDialog.this.dialog.dispose();//��ʱ���Ի�����ʧ
				else
					dialog.setTitle("�����ڽ���"+seconds+"���ر�");
			}
		},1,1,TimeUnit.SECONDS);
		dialog.pack();
		dialog.setSize(new Dimension(350,100));
		dialog.setLocationRelativeTo(father);
		dialog.setVisible(true);
		return result;
		
	}
}
