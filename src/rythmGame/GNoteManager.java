package rythmGame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;


class NoteInfo implements Serializable{
	public int time;
	public int line;
	public int duration;
	public NOTE_TYPE type;
	
	public NoteInfo(int _time, int _line, NOTE_TYPE _type, int _duration) {
		time = _time; line = _line; type = _type; duration = _duration;
	}
}

public class GNoteManager extends GMovable{
	
	private GMusic nowPlaying;
	private int noteIdx = 0;
	private int score = 0;
	private GText scoreText;
	private int maxcombo = 0;
	private int combo = 0;
	private GText comboText;
	private double hp = 10000;
	static final double MAXHP = 10000;
	private int damage = 1;
	private GImage hpBar;
	
	private boolean[] isPressed = {
		false, false, false, false, false
	};
	
	private ArrayList<NoteInfo> noteInfo;
	private LinkedList<GNote>[] notes = new LinkedList[5];
	
	
	@Override
	void Progress() {
		for(int i = noteIdx; i < noteInfo.size(); i++) {
			NoteInfo temp = noteInfo.get(i);
			if(temp.time <= nowPlaying.GetTime() + GNote.REACH_TIME) {
				AddNote(temp.line, temp.type, temp.duration);
				noteIdx++;
				if(noteIdx >= noteInfo.size()-1) {
					new java.util.Timer().schedule(
							new java.util.TimerTask() {
								@Override
								public void run() {
									TestGame.GetInstance().GetScene().DestroyScene();
									TestGame.GetInstance().NextScene(new Scene_complete(maxcombo, score));
									cancel();
								}
								
							}, 7000);
				}
			}
		}
		
		//HP handle
		hp = hp-damage > 0? hp-damage : 0;
		int minus = (int)((1280/MAXHP)*hp - 1280);
		hpBar.SetX(minus);
		if(hp <= 0) {
			TestGame.GetInstance().GetScene().DestroyScene();
			TestGame.GetInstance().NextScene(new Scene_Fail());
		}
		
	}

	public void Judge(int key) {
		if(notes[key].isEmpty())
			return;
		notes[key].getFirst().JudgeAction();
	}
	
	public void AddScore(int add) {
		score += add;
		scoreText.ResetStr(Integer.toString(score));
	}
	
	public void AddHp(double _hp) {
		hp = hp + _hp < MAXHP? hp+_hp : MAXHP;
	}
	
	public void SetKeyState(int key, boolean state) {
		isPressed[key] = state;
	}
	
	public boolean GetKeyState(int key) {
		return isPressed[key];
	}
	
	//int _line,double hitTime, NOTE_TYPE type, int len
	public void AddNote(int key, NOTE_TYPE type, int duration) {
		
		if(type == NOTE_TYPE.LONG) {
			//1초에 진행하는 거리 speed * 0.2 * hitTime -> 노트의 길이 : duration * 1초진행거리
			int len = (int) (GNote.GetSpeed() * 0.2 * duration);
			GNote note = new GNote_Long(key, type, len);
			TestGame.GetInstance().GetScene().AddObj(note);
			notes[key].add(note);
		}
		else if(type == NOTE_TYPE.NORMAL) {
			GNote note = new GNote_normal(key, type);
			TestGame.GetInstance().GetScene().AddObj(note);
			notes[key].add(note);
		}
	}
	
	public void RemoveNote(int key) {
		if(notes[key].isEmpty())
			return;
		notes[key].removeFirst();
	}
	
	public void AddCombo() {
		combo++;
		comboText.ResetStr(combo + " Combo!");
		maxcombo = maxcombo > combo? maxcombo : combo;
	}
	public void ResetCombo() {
		combo = 0;
		comboText.ResetStr(combo + " Combo!");
	}
	public int GetCombo() {
		return combo;
	}
	
	private byte[] Serialize(ArrayList<NoteInfo> obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		objOut.writeObject(obj);
		objOut.close();
		return out.toByteArray();
	}
	
	private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream objin = new ObjectInputStream(in);
		objin.close();
		return objin.readObject();
	}
	
	private void Writefile(String fileName, byte[] data) throws IOException {
		File file = new File("./NoteInfo/" + fileName);
		file.createNewFile();
		Files.write(file.toPath(), data);
	}
	
	private byte[] Readfile(String fileName){
		File file;
		byte[] data = null;
		try {
			byte[] buffer = new byte[4096]; 
			int read = 0;
			InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while((read = is.read(buffer)) != -1) {
				bos.write(buffer);
			}
			data = bos.toByteArray();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	private void WriteNote() {
		ArrayList<NoteInfo> noteInfos = new ArrayList<NoteInfo>();
		//노트 작성
		double gap = 512.82 / 4;	//128.205
		int startTime = (int)gap * 36;
		
		/*
		//0 16 1
		noteInfos.add(new NoteInfo((int)(startTime + gap * 0), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 4), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 6), 2, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 8), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 12), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 14), 4, NOTE_TYPE.NORMAL, 0));
		
		//16 32 2
		
		noteInfos.add(new NoteInfo((int)(startTime + gap * 16), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 20) + (int)(gap * 8), 2, NOTE_TYPE.LONG, (int)(gap * 8)));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 24), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 24), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 28), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 28), 3, NOTE_TYPE.NORMAL, 0));
		
		//32 48 3
		noteInfos.add(new NoteInfo((int)(startTime + gap * 32), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 32), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 36), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 36), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 40) + (int)(gap * 8), 2, NOTE_TYPE.LONG, (int)(gap * 8)));
		
		//48 64 => 4
		noteInfos.add(new NoteInfo((int)(startTime + gap * 48), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 52), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 56), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 60), 3, NOTE_TYPE.NORMAL, 0));
		
		//64 80 => 5
		noteInfos.add(new NoteInfo((int)(startTime + gap * 64), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 68), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 72), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 76), 3, NOTE_TYPE.NORMAL, 0));
		
		//80 96 => 6
		noteInfos.add(new NoteInfo((int)(startTime + gap * 80), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 84), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 88), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 92), 4, NOTE_TYPE.NORMAL, 0));
		
		//96 112 => 7
		noteInfos.add(new NoteInfo((int)(startTime + gap * 96), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 98), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 100), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 104), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 106), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 108), 4, NOTE_TYPE.NORMAL, 0));
		
		//112 128 => 8
		noteInfos.add(new NoteInfo((int)(startTime + gap * 112), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 114), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 116), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 118), 2, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 120), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 122), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 124), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 126), 2, NOTE_TYPE.NORMAL, 0));
		
		//128 144 => 9
		noteInfos.add(new NoteInfo((int)(startTime + gap * 128), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 130), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 132), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 134), 2, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 136), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 138), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 140), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 142), 2, NOTE_TYPE.NORMAL, 0));
				
		//144 160 => 10
		noteInfos.add(new NoteInfo((int)(startTime + gap * 144 + (int)(gap*4)), 0, NOTE_TYPE.LONG, (int)(gap*4)));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 148 + (int)(gap*4)), 1, NOTE_TYPE.LONG, (int)(gap*4)));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 152 + (int)(gap*4)), 3, NOTE_TYPE.LONG, (int)(gap*4)));
		noteInfos.add(new NoteInfo((int)(startTime + gap * 156 + (int)(gap*4)), 4, NOTE_TYPE.LONG, (int)(gap*4)));
		
		//160 176 => 11
		int loc = 160;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4) + (int)(gap*4)), 3, NOTE_TYPE.LONG, (int)(gap*4)));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8) + (int)(gap*4)), 1, NOTE_TYPE.LONG, (int)(gap*4)));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12) + (int)(gap*4)), 0, NOTE_TYPE.LONG, (int)(gap*4)));
		
		//176 192 => 12
		loc = 176;
		noteInfos.add(new NoteInfo((int)(startTime + gap * loc + (int)(gap*12)), 2, NOTE_TYPE.LONG, (int)(gap*12)));
		
		//13
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 3, NOTE_TYPE.NORMAL, 0));
		
		//14
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 4, NOTE_TYPE.NORMAL, 0));
		
		//15
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+2)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+6)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+10)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+14)), 4, NOTE_TYPE.NORMAL, 0));
		
		//16 약38초
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+2)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+6)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+10)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+14)), 4, NOTE_TYPE.NORMAL, 0));
		
		//17
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 0, NOTE_TYPE.NORMAL, 0));
		
		//18
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 2, NOTE_TYPE.NORMAL, 2));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 2, NOTE_TYPE.NORMAL, 2));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 2, NOTE_TYPE.NORMAL, 2));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 1, NOTE_TYPE.NORMAL, 1));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 3, NOTE_TYPE.NORMAL, 3));
		
		//19
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 2, NOTE_TYPE.NORMAL, 2));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 2, NOTE_TYPE.NORMAL, 2));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 2, NOTE_TYPE.NORMAL, 2));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 1, NOTE_TYPE.NORMAL, 1));

		//20
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 3, NOTE_TYPE.NORMAL, 0));
		
		//21
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 4, NOTE_TYPE.NORMAL, 0));
		
		//22
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+2)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+6)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+10)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+14)), 4, NOTE_TYPE.NORMAL, 0));
		
		//23
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+2)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+6)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+10)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+14)), 4, NOTE_TYPE.NORMAL, 0));
		
		//24
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+6)), 2, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 4, NOTE_TYPE.NORMAL, 0));
		
		//25
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+6)), 2, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 0, NOTE_TYPE.NORMAL, 0));
		
		//26
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8) + (int)(gap*4)), 3, NOTE_TYPE.LONG, (int)(gap*4)));
		
		//27
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8) + (int)(gap*4)), 3, NOTE_TYPE.LONG, (int)(gap*4)));
		
		//28
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8) + (int)(gap*4)), 3, NOTE_TYPE.LONG, (int)(gap*4)));
		
		//29
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8) + (int)(gap*4)), 3, NOTE_TYPE.LONG, (int)(gap*4)));
		
		//30
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8) + (int)(gap*4)), 1, NOTE_TYPE.LONG, (int)(gap*4)));
		
		//31
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8) + (int)(gap*4)), 1, NOTE_TYPE.LONG, (int)(gap*4)));
		
		//32
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8) + (int)(gap*4)), 1, NOTE_TYPE.LONG, (int)(gap*4)));
		
		//33
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8) + (int)(gap*4)), 1, NOTE_TYPE.LONG, (int)(gap*4)));

		//34
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 2, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 2, NOTE_TYPE.NORMAL, 0));
		
		//35
		loc += 16;
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc)), 0, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+4)), 1, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+6)), 3, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+8)), 4, NOTE_TYPE.NORMAL, 0));
		noteInfos.add(new NoteInfo((int)(startTime + gap * (loc+12)), 2, NOTE_TYPE.NORMAL, 0));
		*/
		
		//시간순으로 정렬
		Collections.sort(noteInfos, new Comparator<NoteInfo>(){
			 @Override
	            public int compare(NoteInfo n1, NoteInfo n2) {
	                return n1.time - n2.time;
	            }
		});
		
		try {
			byte[] data = Serialize(noteInfos);
			Writefile(nowPlaying.GetName().split("\\.")[0]+".txt", data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void ReadNote() {
		try {
			byte[] data = Readfile(nowPlaying.GetName().split("\\.")[0]+".txt");
			noteInfo = (ArrayList<NoteInfo>)deserialize(data);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public GNoteManager(GMusic _music) {
		super(Type.MOVING);
		// TODO Auto-generated constructor stub
		nowPlaying = _music;
		for(int i = 0; i < 5; i++) {
			notes[i] = new LinkedList<GNote>();
		}
		GNote.SetNoteMgr(this);
		//WriteNote();
		ReadNote();
	}
	
	public void SetScoreText(GText st) {
		scoreText = st;
	}
	
	public void SetComboText(GText ct) {
		comboText = ct;
	}
	
	public void SethpBar(GImage img) {
		hpBar = img;
	}
}
