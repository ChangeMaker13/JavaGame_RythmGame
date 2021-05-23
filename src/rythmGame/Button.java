package rythmGame;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Button extends GameObject{
	
	private ImageIcon normalIcon;
	private ImageIcon activeIcon;
	private JButton button;
	public interface clickAction{
		public void act(MouseEvent event);
	}
	
	public JButton getButton() {
		return button;
	}
	
	public Button(String nomalIconName, String activeIconName, Container container,
			int x, int y, int width, int height, clickAction action) {
		super(Type.BUTTON);
		normalIcon = new ImageIcon(getClass().getClassLoader().getResource(nomalIconName));
		activeIcon = new ImageIcon(getClass().getClassLoader().getResource(activeIconName));
		button = new JButton(normalIcon);

		Initialize(x,y,width, height, action);
		container.add(button);
	}
	
	private void Initialize(int x, int y, int width, int height, clickAction action) {
		button.setBounds(x, y, width, height);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent event) {
				button.setIcon(activeIcon);
				button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent event) {
				button.setIcon(normalIcon);
				button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			public void mousePressed(MouseEvent event) {
				action.act(event);
			}
		});
	}
}
