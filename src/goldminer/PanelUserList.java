package goldminer;
import java.awt.*;
import javax.swing.*;

public class PanelUserList extends JPanel{
	public List userList=new List(10);//8�пͻ���¼
	public PanelUserList()
	{
		this.setLayout(new BorderLayout());
		this.add(userList,BorderLayout.CENTER);
	}

}
