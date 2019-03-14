package goldminer;

//各种服务器、客户端命令的集合
public class Command {
	public static final String TELLNAME="tellName";		//服务器告知客户名字
	public static final String ADD="add";				//客户端被加入到服务器的客户列表中
	public static final String JOIN="join";				//邀请队友加入游戏
	public static final String AGREE="agree";			//接受邀请
	public static final String CHANGE="change";			//状态改变
	public static final String REFUSE="refuse";			//拒绝邀请
	public static final String BEGIN="begin";			//通知玩家可以开始游戏了  begin:
	public static final String OPBEGIN="opbegin";		//通知玩家对方开始了，自动启动游戏
	public static final String LR="LR";					//通知左右  格式为:lr:left   lr:right
	public static final String GO="go";					//放钩子  go:me.isleft(true or false)
	public static final String PAUSE="pause";			//暂停游戏
	public static final String GOON="goon";				//继续
	public static final String FINISH="finish";			//结束游戏
	public static final String TELLRESULT="tellresult";	//告知双方成绩
	public static final String CHAT="chat";				//客户端间协商  客户端为chat:说话    服务器端为chat:说话者:说话
	public static final String BUY="buy";				//决定买升力水
	public static final String NEXTLEVEL="nextlevel";	//进入下一关
	public static final String GIVEUP="giveup";			//放弃游戏
	public static final String AGAIN="again";			//重新游戏
	public static final String QUIT="quit";				//用户退出服务器
	public static final String DELETE="delete";			//删去用户

}
