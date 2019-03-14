package goldminer;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Pig extends Staff{
	ImageIcon imagePig=new ImageIcon("img/pig.png");
	
	int moveSpeed=4;
	int direct;//1���ң�-1����
	
	public Pig(PanelMining father_,int x_,int y_)//λ��
	{
		super(father_,x_,y_);
		value=5;
		backSpeed=3;
		x=x_*father.w;
		if (x_==1)
			direct=-1;
		else if (x==0)
			direct=1;
		sizex=imagePig.getIconWidth();
		sizey=imagePig.getIconHeight();
	}
	
	public void drawPig(Graphics g) {
		g.drawImage(imagePig.getImage(),(int)x,(int)y,father);
	}
	
	public void move()
	{
		x+=direct*moveSpeed;
		if (x+sizex>=father.w||x<=0)
			direct=-direct;
		
	}
}
