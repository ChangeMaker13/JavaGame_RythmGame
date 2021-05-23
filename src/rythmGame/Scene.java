package rythmGame;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import rythmGame.GameObject.Type;

public abstract class Scene {
	public enum SCENE_TYPE{
		INTRO,
		MENU,
		GAME
	}
	
	public static JFrame frame;
	
	protected String SceneName;
	protected ArrayList<GameObject> gRenderable = new ArrayList<GameObject>();
	protected ArrayList<GMovable> gMovable = new ArrayList<GMovable>();
	protected Hashtable<String, GMusic> gMusics = new Hashtable<String, GMusic>();
	
	private static int mouseX, mouseY;
	
	public void AddObj(GameObject obj) {
		if(obj.objType == Type.MUSIC)
			gMusics.put(((GMusic)obj).GetName(), (GMusic)obj);
		else if(obj.objType == Type.MOVING_RENDERING) {
			gMovable.add((GMovable)obj);
			gRenderable.add(obj);
		}
		else if(obj.objType == Type.MOVING) {
			gMovable.add((GMovable)obj);
		}
		else
			gRenderable.add(obj);
	}
	
	public void DestroyObject(GameObject obj) {
		if(obj.getObjType() == Type.MOVING_RENDERING) {
			for(int i = 0; i < gMovable.size(); i++) {
				if(obj.getObjId() == gMovable.get(i).getObjId()) {
					gMovable.remove(i);
					break;
				}
			}
			for(int i = 0; i < gRenderable.size(); i++) {
				if(obj.getObjId() == gRenderable.get(i).getObjId()) {
					gRenderable.remove(i);
					break;
				}
			}
		}
	}
	
	public Scene(String name) {
		SceneName = name;
	}
	
	public void DrawImage(Graphics2D g) {
		for(int i = 0; i < gRenderable.size(); i++){
			gRenderable.get(i).Render(g);
		}
	}
	
	public static void SetFrame(JFrame fra) {
		frame = fra;
	}
	
	public void DestroyScene() {
		
		for(GMusic iter : gMusics.values()){
			//음악 순회하여 모두 Close 하기
			iter.Stop();
		}
	}
	
	public static void AddMenuBar() {
		//exit button
		Button exitButton = new Button("exitButton.png", "exitButton_act.png", frame, 1240, 0, 30, 30, 
				(event) -> {System.exit(0);});
		
		JLabel menuBar = new JLabel(new ImageIcon(Main.class.getClassLoader().getResource("menuBar.png")));
		menuBar.setBounds(0,0,1280,30); 
		menuBar.setVisible(true);
		menuBar.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		menuBar.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event){
				frame.setLocation(event.getXOnScreen() - mouseX,
						event.getYOnScreen() - mouseY);
			}
		});
		frame.add(menuBar);
	}
	public abstract void GameObjectInit();
}	
