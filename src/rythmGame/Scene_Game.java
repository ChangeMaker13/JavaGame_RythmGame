package rythmGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rythmGame.Button.clickAction;

public class Scene_Game extends Scene{
	
	private KeyListener keylistener;
	private String songName;
	private ScheduledExecutorService scheduleService = 
			Executors.newScheduledThreadPool(1);
	private Runnable frameAction = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			FrameAction();
		}
		
	};
	public Scene_Game(String selectedSong) {
		super("Game");
		songName = selectedSong;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void GameObjectInit() {
		// TODO Auto-generated method stub
		//배경화면
		GImage background = new GImage("Song_background.png", 0,0);
		AddObj(background);

		//배경음악
		GMusic bgm = new GMusic(songName, false);
		AddObj(bgm);
		
		//효과음
		GMusic drum = new GMusic("kick.mp3", false);
		AddObj(drum);
		
		GMusic drumStick = new GMusic("snare.mp3", false);
		AddObj(drumStick);
		
		//노트 경로 바
		GImage[] noteRoute = {
				new GImage("noteRoute.png", 390, -50),
				new GImage("noteRoute.png", 490, -50),
				new GImage("noteRoute.png", 590, -50),
				new GImage("noteRoute.png", 690, -50),
				new GImage("noteRoute.png", 790, -50)
		};
		for(int i = 0; i < 5; i++) {
			AddObj(noteRoute[i]);
		}
		
		//이미지
		GImage stateBar = new GImage("State_Bar.png", 0, 670);
		AddObj(stateBar);
		
		GImage judgeBar = new GImage("Judge_Bar.png", 390, 600);
		AddObj(judgeBar);
		
		GImage hpBar = new GImage("hpBar.png", 0, 670);
		AddObj(hpBar);
		
		//폰트
		String arr[] = songName.split("\\.");
		GText songText = new GText(arr[0], 30, 700, "TimesRoman", Font.PLAIN, 30, Color.WHITE);
		AddObj(songText);
		
		GText keyText = new GText("D", 437, 618, "TimesRoman", Font.BOLD, 20, Color.WHITE);
		AddObj(keyText);
		
		keyText = new GText("F", 537, 618, "TimesRoman", Font.BOLD, 20, Color.WHITE);
		AddObj(keyText);
		
		keyText = new GText("SPACE", 610, 618, "TimesRoman", Font.BOLD, 20, Color.WHITE);
		AddObj(keyText);
		
		keyText = new GText("J", 737, 618, "TimesRoman", Font.BOLD, 20, Color.WHITE);
		AddObj(keyText);
		
		keyText = new GText("K", 837, 618, "TimesRoman", Font.BOLD, 20, Color.WHITE);
		AddObj(keyText);
		
		GText scoreText = new GText(Integer.toString(0), 100, 200, "TimesRoman", Font.PLAIN, 45, Color.GREEN);
		AddObj(scoreText);
		
		GText comboText = new GText(Integer.toString(0) + " Combo!", 900, 200, "TimesRoman", Font.PLAIN, 45, Color.WHITE);
		AddObj(comboText);
		
		//노트매니져
		GNoteManager noteMgr = new GNoteManager(gMusics.get(songName));
		AddObj(noteMgr);
		noteMgr.SetScoreText(scoreText);
		noteMgr.SetComboText(comboText);
		noteMgr.SethpBar(hpBar);
		
		//키
		keylistener = new KeyListener();
		keylistener.AddAction(new KeyAction() {
			
			{keyCode = KeyEvent.VK_D;}
			
			@Override
			public void ReleaseAction() {
				// TODO Auto-generated method stub
				ReleaseKey(0, noteMgr, noteRoute[0]);
			}
			
			@Override
			public void PressAction() {
				// TODO Auto-generated method stub
				PressKey(0, noteMgr, noteRoute[0]);
			}
		});
		keylistener.AddAction(new KeyAction() {
			
			{keyCode = KeyEvent.VK_F;}
			
			@Override
			public void ReleaseAction() {
				// TODO Auto-generated method stub
				ReleaseKey(1, noteMgr, noteRoute[1]);
			}
			
			@Override
			public void PressAction() {
				// TODO Auto-generated method stub
				PressKey(1, noteMgr, noteRoute[1]);
			}
		});
		keylistener.AddAction(new KeyAction() {
			
			{keyCode = KeyEvent.VK_SPACE;}
			
			@Override
			public void ReleaseAction() {
				// TODO Auto-generated method stub
				ReleaseKey(2, noteMgr, noteRoute[2]);
			}
			
			@Override
			public void PressAction() {
				// TODO Auto-generated method stub
				PressKey(2, noteMgr, noteRoute[2]);
			}
		});
		keylistener.AddAction(new KeyAction() {
			
			{keyCode = KeyEvent.VK_J;}
			
			@Override
			public void ReleaseAction() {
				// TODO Auto-generated method stub
				ReleaseKey(3, noteMgr, noteRoute[3]);
			}
			
			@Override
			public void PressAction() {
				// TODO Auto-generated method stub
				PressKey(3, noteMgr, noteRoute[3]);
			}
		});
		keylistener.AddAction(new KeyAction() {
			
			{keyCode = KeyEvent.VK_K;}
			
			@Override
			public void ReleaseAction() {
				// TODO Auto-generated method stub
				ReleaseKey(4, noteMgr, noteRoute[4]);
			}
			
			@Override
			public void PressAction() {
				// TODO Auto-generated method stub
				PressKey(4, noteMgr, noteRoute[4]);
			}
		});
		
		frame.addKeyListener(keylistener);
		frame.requestFocus();

		gMusics.get(songName).Play();
		
		//되돌아가기 버튼
		Button returnBtn = new Button("return.png", "return.png", frame, 1180, 50, 100,100, new clickAction() {
			@Override
			public void act(MouseEvent event) {
				// TODO Auto-generated method stub
				DestroyScene();
				TestGame.GetInstance().NextScene(new Scene_menu());
			}
		});
		
		
		//스레드 시작
		scheduleService.scheduleAtFixedRate(frameAction, 0, 5, TimeUnit.MILLISECONDS);
	}
	
	private void PressKey(int key, GNoteManager noteMgr, GImage noteRoute) {
		if(noteMgr.GetKeyState(key) == false) {
			noteMgr.SetKeyState(key, true);
			noteRoute.ChangeImage("noteRoute_active.png");
			if(key != 2)
				gMusics.get("kick.mp3").Play();
			else if(key == 2)
				gMusics.get("snare.mp3").Play();
			noteMgr.Judge(key);
		}
	}
	
	private void ReleaseKey(int key, GNoteManager noteMgr, GImage noteRoute) {
		noteMgr.SetKeyState(key, false);
		noteRoute.ChangeImage("noteRoute.png");
	}

	
	public void FrameAction() {
		for(int i = 0; i < gMovable.size(); i++){
			gMovable.get(i).Progress();
		}
	}
	
	@Override
	public void DestroyScene() {
		for(GMusic iter : gMusics.values()){
			//음악 순회하여 모두 Close 하기
			iter.Stop();
		}
		scheduleService.shutdownNow();
		frame.removeKeyListener(keylistener);
	}
}
