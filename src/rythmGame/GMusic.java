package rythmGame;

public class GMusic extends GameObject{
	
	private Music player;
	private String name;
	private boolean isLoop;

	public GMusic(String _name, boolean _isLoop) {
		super(GameObject.Type.MUSIC);
		// TODO Auto-generated constructor stub
		name = _name;
		isLoop = _isLoop;
	}
	
	public void Play() {
		player = new Music(name, isLoop);
		player.start();
	}
	
	public void Stop() {
		if(player != null)
			player.close();
		player = null;
	}
	
	public String GetName() {
		return name;
	}
	
	public int GetTime() {
		return player.getTime();
	}
}
