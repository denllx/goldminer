package goldminer;
import java.util.*;

public class DoubleRecord extends SingleRecord{
	String opName;
	
	public DoubleRecord(int score_,Date date_,String name){
		super(score_,date_);
		opName=name;
	}

}
