package goldminer;
import java.awt.*;
import javax.swing.*;

public class PanelUserList extends JPanel{
	public List userList=new List(10);//8行客户记录
	public PanelUserList()
	{
		this.setLayout(new BorderLayout());
		this.add(userList,BorderLayout.CENTER);
	}

}
