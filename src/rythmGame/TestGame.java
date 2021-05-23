package rythmGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;

public class TestGame extends JFrame {
	private Scene scene = new Scene_Intro();
	private Image screenImage;
	private Graphics screenGraphic;
	
	
	private static class InnerClass{
		private static final TestGame instance = new TestGame();
	}
	
	public static TestGame GetInstance() {
		return InnerClass.instance;
	}
	
	public Scene GetScene() {
		return scene;
	}
	
	private TestGame() {
		setUndecorated(true);
		setTitle("Rythm Game");
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0,0,0,0));
		setLayout(null);
		
		Scene.SetFrame(this);
		Scene.AddMenuBar();
		scene.GameObjectInit();
	}
	
	public void NextScene(Scene newScene) {
		scene = newScene;
		//그냥 removeAll을 해버리면 contentPane 도 사라져버려서 컴포넌트를 추가할수 없게됨.
		this.getContentPane().removeAll();
		Scene.AddMenuBar();
		scene.GameObjectInit();
	}
	
	public void paint(Graphics g) {
		screenImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		screenGraphic = screenImage.getGraphics();
		DrawImage((Graphics2D)screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
		repaint();
	}
	
	public void DrawImage(Graphics2D g)
	{
		scene.DrawImage(g);
		paintComponents(g);
	}
}
