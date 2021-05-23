package rythmGame;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class GImage extends GameObject{
	private Image image;
	private int xpos;
	private int ypos;
	
	public GImage(String fileName, int x, int y) {
		super(Type.IMAGE);
		image = new ImageIcon
				(getClass().getClassLoader().getResource(fileName)).getImage();
		xpos = x;
		ypos = y;
	}
	
	public void Render(Graphics2D g) {
		g.drawImage(image, xpos, ypos, null);
	}
	
	public void ChangeImage(String fileName) {
		image = new ImageIcon
				(getClass().getClassLoader().getResource(fileName)).getImage();
	}
	
	public void SetY(int y) {
		ypos = y;
	}
	public void SetX(int x) {
		xpos = x;
	}
}
