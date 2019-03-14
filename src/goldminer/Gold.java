package goldminer;
import java.awt.Graphics;

import javax.swing.*;

public class Gold extends Staff{
	int size;
	int[] values= {100,200,500};
	
	ImageIcon imageGold;
	String[] imageName=new String[3];
	
	int[] speeds= {5,3,2};//钩子拖动的速度

	
	public Gold(PanelMining father_,int x_,int y_,int size_) {
		super(father_,x_,y_);
		
		size=size_;
		
		imageName[0]="img/smallgold.png";
		imageName[1]="img/midgold.png";
		imageName[2]="img/biggold.png";
		imageGold=new ImageIcon(imageName[size-1]);
		value=values[size-1];
		backSpeed=speeds[size-1];
		sizex=imageGold.getIconWidth();
		sizey=imageGold.getIconHeight();
	}
	
	public void drawGold(Graphics g) {
		g.drawImage(imageGold.getImage(),(int)x,(int)y,father);
	}
	

}
