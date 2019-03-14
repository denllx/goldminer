package goldminer;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.*;

public class PanelChat extends JPanel{

	public JTextArea textChat;
	public JButton buttonSend,buttonClear;
	public JTextArea chatHistory;
	public JLabel labelTip;
	public JScrollPane scroll;
	
	public PanelChat()
	{
		this.setLayout(new BorderLayout());
		textChat=new JTextArea("",2,20);
		chatHistory=new JTextArea("",8,20);
		scroll=new JScrollPane(chatHistory);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		buttonSend=new JButton("·¢ËÍ");
		buttonClear=new JButton("Çå¿Õ");
		buttonSend.setFocusable(false);
		buttonClear.setFocusable(false);
		chatHistory.setEditable(false);
		textChat.setBorder(new LineBorder(new Color(0,0,255)));
		labelTip=new JLabel("Chat here ^-^");
		labelTip.setFont(new Font("Times New Roman",Font.BOLD,20));
		labelTip.setBackground(null);
		labelTip.setForeground(new Color(0,100,0));
		
		
		this.add(scroll, BorderLayout.NORTH);
		JPanel panelText=new JPanel(new BorderLayout());
		panelText.add(textChat,BorderLayout.SOUTH);
		panelText.add(labelTip, BorderLayout.NORTH);
		this.add(panelText,BorderLayout.CENTER);
		JPanel panelButton=new JPanel(new FlowLayout());
		panelButton.add(buttonSend);
		panelButton.add(buttonClear);
		this.add(panelButton, BorderLayout.SOUTH);
		
	}
}
