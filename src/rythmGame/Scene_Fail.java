package rythmGame;

public class Scene_Fail extends Scene{

	public Scene_Fail() {
		super("fail");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void GameObjectInit() {
		// TODO Auto-generated method stub
		
		//���ȭ��
		GImage background = new GImage("background_fail.png", 0, 0);
		AddObj(background);
		
		//���� ��ư
		Button nextBtn = new Button("nextBtn.png", "nextBtn.png",
				frame, 1180, 620,100,100,
				(event)->{
					DestroyScene();
					TestGame.GetInstance().NextScene(new Scene_Intro());
				});
		AddObj(nextBtn);
	}

}
