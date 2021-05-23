package rythmGame;

import java.awt.Graphics2D;

public abstract class GameObject {
	
	enum Type{
		BUTTON,
		IMAGE,
		MUSIC,
		TEXT,
		MOVING_RENDERING,
		MOVING,
		ETC
	}
	
	protected int objId;
	
	public int getObjId() {
		return objId;
	}

	private static int nextId = 1;
	
	protected Type objType;
	
	public Type getObjType() {
		return objType;
	}

	public GameObject(Type objtype){
		objType = objtype;
		objId = nextId++;
	}
	
	public void Render(Graphics2D g) {};
}