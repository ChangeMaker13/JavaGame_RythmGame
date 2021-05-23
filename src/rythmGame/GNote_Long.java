package rythmGame;

import java.awt.Graphics2D;

import rythmGame.GJudgeIcon.JUDGE;

public class GNote_Long extends GNote{
	boolean isDead = false;
	boolean isActivated = false;
	private int length;
	private int timer = 0;
	
	public GNote_Long(int _line, NOTE_TYPE type, int len) {
		super(_line, type);
		length = len;
	}
	
	public void Render(Graphics2D g) {
		if(ypos + length > 670)
			length = (int) (670 - ypos);
		g.drawImage(noteImages.get(noteType), (int)xpos, (int)ypos, 100, length, null);
	};
	
	public void Progress() {
		ypos += speed;
		//화면을 벗어나면 노트를 지워준다.
		if(ypos >= 650) {
			if(isDead == true) {
				TestGame.GetInstance().GetScene().DestroyObject(this);
			}
			else {
				GJudgeIcon icon = new GJudgeIcon(JUDGE.MISS, 290 + (key+1)*100, 620);
				TestGame.GetInstance().GetScene().AddObj(icon);
				noteMgr.AddScore((int)((1 + noteMgr.GetCombo()*0.01) * JUDGE.MISS.getScore()));
				double hpToAdd = (1 + noteMgr.GetCombo()*0.01) * JUDGE.MISS.getScore();
				noteMgr.AddHp(hpToAdd > JUDGE.MISS.getScore()? hpToAdd : JUDGE.MISS.getScore());
				
				TestGame.GetInstance().GetScene().DestroyObject(this);
				noteMgr.RemoveNote(key);
				noteMgr.ResetCombo();
			}
		}
		
		//활성화된 상태에서 키를 누르고 있으면 점수가 계속 오른다
		if(isActivated == true) {
			if(noteMgr.GetKeyState(key) == true) {
				if(timer % 5 == 0)
				{
					noteMgr.AddScore(5);
					noteMgr.AddCombo();
				}
				length -= speed;
				timer += 1;
			}
			else {
				JUDGE judge = JudgeStandard(ypos);
				GJudgeIcon icon = new GJudgeIcon(judge, 290 + (key+1)*100, 620);
				TestGame.GetInstance().GetScene().AddObj(icon);
				noteMgr.AddScore((int)((1 + noteMgr.GetCombo()*0.01) * judge.getScore()));
				double hpToAdd = (1 + noteMgr.GetCombo()*0.01) * judge.getScore();
				noteMgr.AddHp(hpToAdd > judge.getScore()? hpToAdd : judge.getScore());
				
				if(judge != JUDGE.MISS && judge != JUDGE.NONE) {
					TestGame.GetInstance().GetScene().DestroyObject(this);
					noteMgr.RemoveNote(key);
				}
				else {
					if(judge == JUDGE.NONE) {
						icon = new GJudgeIcon(JUDGE.MISS, 290 + (key+1)*100, 620);
						TestGame.GetInstance().GetScene().AddObj(icon);
						noteMgr.AddScore(JUDGE.MISS.getScore());
					}
					isDead = true;
					isActivated = false;
					noteType = NOTE_TYPE.DEAD;
					noteMgr.RemoveNote(key);
					noteMgr.ResetCombo();
				}
			}
		}
	}
	
	@Override
	public void JudgeAction() {
		// TODO Auto-generated method stub
		JUDGE judge = JudgeStandard(ypos + length - 25);
		if(judge == JUDGE.NONE)
			return;
		GJudgeIcon icon = new GJudgeIcon(judge, 290 + (key+1)*100, 620);
		TestGame.GetInstance().GetScene().AddObj(icon);
		noteMgr.AddScore((int)((1 + noteMgr.GetCombo()*0.01) * judge.getScore()));
		double hpToAdd = (1 + noteMgr.GetCombo()*0.01) * judge.getScore();
		noteMgr.AddHp(hpToAdd > judge.getScore()? hpToAdd : judge.getScore());
		
		if(judge != JUDGE.MISS)
			isActivated = true;
		else {
			isDead = true;
			isActivated = false;
			noteType = NOTE_TYPE.DEAD;
			noteMgr.RemoveNote(key);
			noteMgr.ResetCombo();
		}
	}
}
