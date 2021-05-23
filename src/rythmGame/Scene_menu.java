package rythmGame;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import rythmGame.Button.clickAction;

public class Scene_menu extends Scene{
	
	private ArrayList<Track> trackList = 
			new ArrayList<Track>();
	private int nowSelected = 0;
	
	public Scene_menu() {
		super("menu");
		// TODO Auto-generated constructor stub
		
		//track set
		trackList.add(new Track(0, "title_Word.png", "niniz.png", "Anna Graceman - Words.mp3", 117));
		trackList.add(new Track(1, "title_Grand.png", "jordi.png", "Grandpapa_Eleven_Month.mp3", 130));
		trackList.add(new Track(2, "title_Linda.png", "lion.png", "Linda.mp3",(int)121.35));
	}

	@Override
	public void GameObjectInit() {
		// TODO Auto-generated method stub
		//음악 추가
		for(Track track : trackList) {
			GMusic music = new GMusic(track.getSongName(), true);
			AddObj(music);
		}
		gMusics.get(trackList.get(0).getSongName()).Play();
		
		//배경화면
		GImage background = new GImage("inGame_background.jpg", 0, 0);
		AddObj(background);
		
		//선택된 이미지
		GImage selected = new GImage(trackList.get(0).getSongImage(), 465, 185);
		AddObj(selected);
		
		//제목 이미지
		GImage title = new GImage(trackList.get(0).getTitleImage(), 600, 180);
		AddObj(title);
		
		//선택버튼
		Button selectButton = new Button("vacant.png", "vacant.png", frame, 465, 185, 350,350,
				(event) -> {
					DestroyScene();
					TestGame.GetInstance().NextScene(new Scene_Game(trackList.get(nowSelected).getSongName()));
				});
		AddObj(selectButton);
		
		//오른쪽 버튼
		Button rightButon = new Button("rightBtn.png", "rightBtn.png", frame,855, 360, 50, 50, 
				(event) -> {
					gMusics.get(trackList.get(nowSelected).getSongName()).Stop();
					
					if(nowSelected == trackList.size()-1)
						nowSelected = 0;
					else nowSelected++;
					selected.ChangeImage(trackList.get(nowSelected).getSongImage());
					title.ChangeImage(trackList.get(nowSelected).getTitleImage());
					
					gMusics.get(trackList.get(nowSelected).getSongName()).Play();
				});
		AddObj(rightButon);
		
		//왼쪽 버튼
		Button leftButton = new Button("leftBtn.png", "leftBtn.png", frame,375, 360, 50, 50, 
				(event) -> {
					gMusics.get(trackList.get(nowSelected).getSongName()).Stop();
					
					if(nowSelected == 0)
						nowSelected = trackList.size()-1;
					else nowSelected--;
					selected.ChangeImage(trackList.get(nowSelected).getSongImage());
					title.ChangeImage(trackList.get(nowSelected).getTitleImage());
					
					gMusics.get(trackList.get(nowSelected).getSongName()).Play();
				});
		AddObj(leftButton);
		
		//되돌아가기 버튼
		Button returnBtn = new Button("return.png", "return.png", frame, 1180, 50, 100,100, new clickAction() {
			@Override
			public void act(MouseEvent event) {
				// TODO Auto-generated method stub
				DestroyScene();
				TestGame.GetInstance().NextScene(new Scene_Intro());
			}
		});
		
	}
}
