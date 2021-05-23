package rythmGame;

public class Scene_Intro extends Scene{
	
	public Scene_Intro() {
		super("Intro");
		// TODO Auto-generated constructor stub
	}
	
	public void GameObjectInit() {
		//오브젝트 세팅
		//배경화면
		GImage background = new GImage("Intro_background.jpg", 0, 0);
		AddObj(background);
		
		//음악 재생
		GMusic introMusic = new GMusic("Undertale+Ost_100+-+Megalovania.mp3", true);
		AddObj(introMusic);
		introMusic.Play();
		
		//시작 버튼
		Button startBtn = new Button("startBtn.png", "startBtn_active.png",
				frame, 30, 100,200,50,
				(event)->{
					DestroyScene();
					TestGame.GetInstance().NextScene(new Scene_menu());
				});
		AddObj(startBtn);
		
		//나가기 버튼
		Button exitBtn = new Button("endBtn.png", "endBtn_active.png",
				frame, 30, 200, 200, 50,
				(event)->{
					System.exit(0);
				});
		AddObj(exitBtn);
	}
}
