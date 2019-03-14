package goldminer;
import java.util.*;

public class SingleRecord {
	int score;
	Date date;//游戏结束时间
	
	public SingleRecord(int score_,Date date_)
	{
		score=score_;
		date=date_;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public Date getDate()
	{
		return date;
	}
}
