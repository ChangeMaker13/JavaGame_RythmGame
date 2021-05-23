package rythmGame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Hashtable;

import javax.swing.ImageIcon;

public class GJudgeIcon extends GMovable{
	
	public enum JUDGE{
		MISS(-100),
		GOOD(50),
		NICE(100),
		PERFECT(150),
		NONE(0);
		
		private int score;
		
		private JUDGE(int value) {score = value;}
		
		public int getScore() {
			return score;
		}
	}
	private static Hashtable<JUDGE, Image> icons = new Hashtable<JUDGE, Image>();
	private static final int duration = 30;
	
	private JUDGE judgeType;
	private int xpos, ypos;
	private int remainTime = duration;
	private int width = 75;
	private int height = 50;
	
	static {
		Image icon = new ImageIcon(Main.class.getClassLoader().getResource("miss.png")).getImage();
		icons.put(JUDGE.MISS, icon);
		
		icon = new ImageIcon(Main.class.getClassLoader().getResource("good.png")).getImage();
		icons.put(JUDGE.GOOD, icon);
		
		icon = new ImageIcon(Main.class.getClassLoader().getResource("nice.png")).getImage();
		icons.put(JUDGE.NICE, icon);
		
		icon = new ImageIcon(Main.class.getClassLoader().getResource("perfect.png")).getImage();
		icons.put(JUDGE.PERFECT, icon);
	}
	
	public void Render(Graphics2D g) {
		g.drawImage(icons.get(judgeType), xpos, ypos, width, height, null);
	};

	public void Progress() {
		width -= 0.00001;
		height -= 0.00001;
		remainTime--;
		if(remainTime < 0)
			TestGame.GetInstance().GetScene().DestroyObject(this);
	}

	public GJudgeIcon(JUDGE grade, int x, int y) {
		super(Type.MOVING_RENDERING);
		// TODO Auto-generated constructor stub
		judgeType = grade;
		xpos = x; ypos = y;
	}

}
