package goldminer;


public class Staff {
	double x,y;//λ��
	int value;//��ֵ
	PanelMining father;
	boolean isCatched=false;
	int sizex,sizey;
	int master;
	boolean arrive=false;
	int backSpeed;
	double timeleft=0.5;
	
	public Staff(PanelMining father_,double x_,double y_)
	{
		father=father_;
		x=x_;
		y=y_;
	}
	
	boolean cover(double x1,double y1)
	{
		if (x1>=x&&x1<=x+sizex&&y1>=y&&y1<=y+sizey)
			return true;
		return false;
	}
	
	
	boolean getCaught(double currentx1,double currenty1,int hookSize,double currentAngle)
	{
		double tempx=currentx1+hookSize*1.5*Math.cos(currentAngle);
		double tempy=currenty1+hookSize*1.5*Math.sin(currentAngle);
		if ((tempx>=x				//�����
				&&tempx<=x+sizex)	//���ұ�
				&&tempy>=y			//������
				&&tempy<=y+sizey)	//������
			return true;
		return false;
	}
	
	public void back(double backSpeed,double currentAngle)
	{
		x-=backSpeed*Math.cos(currentAngle);
		y-=backSpeed*Math.sin(currentAngle);
	}
}
