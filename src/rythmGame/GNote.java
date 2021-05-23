package rythmGame;

import java.awt.Image;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import rythmGame.GJudgeIcon.JUDGE;

enum NOTE_TYPE {
	NORMAL,
	LONG,
	DEAD,
}

public abstract class GNote extends GMovable{

	protected static GNoteManager noteMgr;
	protected static Hashtable<NOTE_TYPE, Image> noteImages = 
			new Hashtable<NOTE_TYPE, Image>();
	protected static int speed;
	protected static final int REACH_TIME = 3000;

	protected NOTE_TYPE noteType;
	protected double xpos, ypos;
	protected int key;

	
	static {
		Image normalnote = new ImageIcon(Main.class.getClassLoader().getResource("note.png")).getImage();
		noteImages.put(NOTE_TYPE.NORMAL, normalnote);
		noteImages.put(NOTE_TYPE.LONG, normalnote);
		
		Image deadnote = new ImageIcon(Main.class.getClassLoader().getResource("note_dead.png")).getImage();
		noteImages.put(NOTE_TYPE.DEAD, deadnote);
		speed = 2;
	}
	
	public static int GetSpeed() {
		return speed;
	}
	
	public double Getypos() {
		return ypos;
	}
	
	static public void SetNoteMgr(GNoteManager _mgr) {
		noteMgr = _mgr;
	}
	
	protected JUDGE JudgeStandard(double ypos) {
		if(ypos >= 575 && ypos <= 580)	return JUDGE.GOOD;		
		else if(ypos >= 580 && ypos < 590)	return JUDGE.NICE;
		else if(ypos >= 590 && ypos < 610) return JUDGE.PERFECT;
		else if(ypos >= 610 && ypos < 620) return JUDGE.NICE;
		else if(ypos >= 620 && ypos < 625) return JUDGE.GOOD;
		else if(ypos >= 625) return JUDGE.MISS;
		else return JUDGE.NONE;
	}
	
	abstract public void JudgeAction();
	
	public GNote(int _key, NOTE_TYPE type) {
		super(Type.MOVING_RENDERING);
		// TODO Auto-generated constructor stub
		key = _key;
		int startPos = 290;
		xpos = startPos + (key+1)*100;
		ypos = 600 - (speed * 0.2 * REACH_TIME);
		noteType = type;
	}
}
