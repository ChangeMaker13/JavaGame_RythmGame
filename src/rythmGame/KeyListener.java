package rythmGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

abstract class KeyAction {
	protected int keyCode;
	public abstract void PressAction();
	public abstract void ReleaseAction();
}

public class KeyListener extends KeyAdapter{
	
	private ArrayList<KeyAction> actionArray = new ArrayList<KeyAction>();
	
	public void AddAction(KeyAction act) {
		actionArray.add(act);
	}
	
	public void RemoveAction(int key) {
		for(int i = 0; i < actionArray.size(); i++) {
			if(key == actionArray.get(i).keyCode) {
				actionArray.remove(i);
				return;
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		for(int i = 0; i < actionArray.size(); i++) {
			if(e.getKeyCode() == actionArray.get(i).keyCode) {
				actionArray.get(i).PressAction();
				break;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		for(int i = 0; i < actionArray.size(); i++) {
			if(e.getKeyCode() == actionArray.get(i).keyCode) {
				actionArray.get(i).ReleaseAction();
				break;
			}
		}
	}
}
