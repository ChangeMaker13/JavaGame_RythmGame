package rythmGame;

import java.awt.Color;
import java.awt.Font;

public class Scene_complete extends Scene{
	
	private int maxCombo;
	private int score;

	public Scene_complete(int _maxCombo, int _score) {
		super("complete");
		maxCombo = _maxCombo;
		score = _score;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void GameObjectInit() {
		// TODO Auto-generated method stub
		
		//배경화면
		GImage background = new GImage("background_complete.jpg", 0, 0);
		AddObj(background);
		
		//다음 버튼
		Button nextBtn = new Button("nextBtn.png", "nextBtn.png",
				frame, 1180, 620,100,100,
				(event)->{
					DestroyScene();
					TestGame.GetInstance().NextScene(new Scene_Intro());
				});
		AddObj(nextBtn);
		
		//폰트
		GText maxComboText = new GText("MAX COMBO", 100, 200, "TimesRoman", Font.BOLD, 50, Color.WHITE);
		AddObj(maxComboText);
		
		maxComboText = new GText(Integer.toString(maxCombo), 100, 300, "TimesRoman", Font.BOLD, 100, Color.WHITE);
		AddObj(maxComboText);
		
		GText scoreText = new GText("SCORE", 100, 450, "TimesRoman", Font.BOLD, 50, Color.GREEN);
		AddObj(scoreText);
		
		scoreText = new GText(Integer.toString(score), 100, 550, "TimesRoman", Font.BOLD, 100, Color.GREEN);
		AddObj(scoreText);
	}

}
