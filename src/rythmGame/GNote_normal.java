package rythmGame;

import java.awt.Graphics2D;

import rythmGame.GJudgeIcon.JUDGE;

public class GNote_normal extends GNote{

	public GNote_normal(int _line , NOTE_TYPE type) {
		super(_line, type);
	}
	
	public void Render(Graphics2D g) {
		g.drawImage(noteImages.get(noteType), (int)xpos, (int)ypos, null);
	};
	
	public void Progress() {
		ypos += speed;
		//화면을 벗어나면 노트를 지워준다.
		if(ypos >= 650) {
			JudgeAction();
		}
	}
	
	@Override
	public void JudgeAction() {
		// TODO Auto-generated method stub
		JUDGE judge = JudgeStandard(ypos);
		if(judge == JUDGE.NONE) return;
		
		GJudgeIcon icon = new GJudgeIcon(judge, 290 + (key+1)*100, 620);
		TestGame.GetInstance().GetScene().AddObj(icon);
		noteMgr.AddScore((int)((1 + noteMgr.GetCombo()*0.01) * judge.getScore()));
		double hpToAdd = (1 + noteMgr.GetCombo()*0.01) * judge.getScore();
		noteMgr.AddHp(hpToAdd > judge.getScore()? hpToAdd : judge.getScore());
		TestGame.GetInstance().GetScene().DestroyObject(this);
		noteMgr.RemoveNote(key);
		
		if(judge == JUDGE.MISS) {
			noteMgr.ResetCombo();
		}
		else {
			noteMgr.AddCombo();
		}
	}
}
