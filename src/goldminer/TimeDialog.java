package goldminer;
import java.util.concurrent.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//提示玩家是否接受邀请的定时对话框
public class TimeDialog {
	private String message=null;
	private int seconds=0;//可思考时间
	private JLabel label=new JLabel();
	private JButton confirm;//接受邀请
	private JButton cancel;//不接受邀请
	private JDialog dialog=null;
	int result=-5;//接受为0，,拒绝为1，超时为-5
	
	public int showDialog(JFrame father,String message_,int sec)
	{
		this.message=message_;//玩家邀请您，您是否同意？
		seconds=sec;
		label.setText(message_);
		label.setBounds(80,6,200,20);
		ScheduledExecutorService s=Executors.newSingleThreadScheduledExecutor();
		confirm=new JButton("接受");
		confirm.setBounds(100,40,60,20);
		
		//如果按下接受键，result=0,对话框消失
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				result=0;
				TimeDialog.this.dialog.dispose();
			}
		});
		cancel=new JButton("拒绝");
		cancel.setBounds(190,40,60,20);
		
		//如果按下“拒绝”键，result=1,对话框消失
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				result=1;
				TimeDialog.this.dialog.dispose();
			}
		});
		
		//绘制对话框
		dialog=new JDialog(father,true);//对话框是模态框，父窗口是father
		dialog.setTitle("提示：本窗口将在"+seconds+"秒后关闭");
		dialog.setLayout(null);
		dialog.add(label);//玩家邀请您，您是否同意？
		dialog.add(confirm);
		dialog.add(cancel);
		
		//runnable任务按指定的时间间隔重复执行
		s.scheduleAtFixedRate(new Runnable() {
			public void run()
			{
				TimeDialog.this.seconds--;
				if (TimeDialog.this.seconds==0)
					TimeDialog.this.dialog.dispose();//超时，对话框消失
				else
					dialog.setTitle("本窗口将在"+seconds+"秒后关闭");
			}
		},1,1,TimeUnit.SECONDS);
		dialog.pack();
		dialog.setSize(new Dimension(350,100));
		dialog.setLocationRelativeTo(father);
		dialog.setVisible(true);
		return result;
		
	}
}
