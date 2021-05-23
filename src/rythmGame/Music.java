package rythmGame;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javazoom.jl.player.Player;

public class Music extends Thread{
	
	private Player player;
	private boolean isLoop;
	private BufferedInputStream bis;
	private String name;
	
	public Music(String _name, boolean _isLoop)
	{
		name = _name;
		isLoop = _isLoop;
		try {
			InputStream is = ClassLoader.getSystemResourceAsStream(name);
			bis = new BufferedInputStream(is);
			player = new Player(bis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getTime()
	{
		if(player == null)
			return 0;
		return player.getPosition();
	}
	
	public void close()
	{
		isLoop = false;
		player.close();
		this.interrupt();
	}
	
	public void run()
	{
		try {
			do {
				player.play();
				InputStream is = ClassLoader.getSystemResourceAsStream(name);
				bis = new BufferedInputStream(is);
				player = new Player(bis);
			}while(isLoop);
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
