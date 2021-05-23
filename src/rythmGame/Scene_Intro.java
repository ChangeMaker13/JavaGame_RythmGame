package rythmGame;

public class Scene_Intro extends Scene{
	
	public Scene_Intro() {
		super("Intro");
		// TODO Auto-generated constructor stub
	}
	
	public void GameObjectInit() {
		//������Ʈ ����
		//���ȭ��
		GImage background = new GImage("Intro_background.jpg", 0, 0);
		AddObj(background);
		
		//���� ���
		GMusic introMusic = new GMusic("Undertale+Ost_100+-+Megalovania.mp3", true);
		AddObj(introMusic);
		introMusic.Play();
		
		//���� ��ư
		Button startBtn = new Button("startBtn.png", "startBtn_active.png",
				frame, 30, 100,200,50,
				(event)->{
					DestroyScene();
					TestGame.GetInstance().NextScene(new Scene_menu());
				});
		AddObj(startBtn);
		
		//������ ��ư
		Button exitBtn = new Button("endBtn.png", "endBtn_active.png",
				frame, 30, 200, 200, 50,
				(event)->{
					System.exit(0);
				});
		AddObj(exitBtn);
	}
}
