package goldminer;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class NewButton extends JButton{
	ImageIcon img; 
	public NewButton(String icon){ 
	  super(); 
	  this.img = new ImageIcon(icon); 
	  setBorderPainted(false); 
	  setContentAreaFilled(false); 
	  setOpaque(false); 
	  setSize(img.getIconWidth(),img.getIconHeight()); 
	}
	 
	 @Override
	 public void paintComponent(Graphics g){ 
	  if(this.getModel().isPressed())
	  { 
		  g.drawImage(img.getImage(),1,1,this); 
	  }
	  else
	  { 
		  g.drawImage(img.getImage(),0,0,this); 
	  } 
	  super.paintComponent(g); 
	 } 
}