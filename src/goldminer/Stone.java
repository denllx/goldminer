package goldminer;
import java.awt.*;
import javax.swing.*;

public class Stone extends Staff{
	

	ImageIcon imageStone=new ImageIcon("img/stone.png");
	
	public Stone(PanelMining father_,int x_,int y_)//Œª÷√
	{
		super(father_,x_,y_);
		
		sizex=imageStone.getIconWidth();
		sizey=imageStone.getIconHeight();
		value=20;
		backSpeed=1;
	}
	
	public void drawStone(Graphics g) {
		g.drawImage(imageStone.getImage(),(int)x,(int)y,father);
	}
	
	
}
