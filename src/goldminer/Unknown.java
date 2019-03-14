package goldminer;
import javax.swing.*;
import java.util.*;
import java.awt.*;

public class Unknown extends Staff{
	ImageIcon imageUnknown=new ImageIcon("img/unknown.png");
	
	
	public Unknown(PanelMining father_,int x_,int y_,int value_,int speed_)//Œª÷√
	{
		super(father_,x_,y_);
		sizex=imageUnknown.getIconWidth();
		sizey=imageUnknown.getIconHeight();
		backSpeed=speed_;
		value=value_;
	}
	
	public void drawUnknown(Graphics g) {
		g.drawImage(imageUnknown.getImage(),(int)x,(int)y,father);
	}
	
	public void setValue(int value_)
	{
		value=value_;
	}


}
