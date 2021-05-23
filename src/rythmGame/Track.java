package rythmGame;

public class Track {
	
	private int trackNum;
	private String titleImage;
	private String songImage;
	private String songName;
	private int bpm;
	
	
	public int getBpm() {
		return bpm;
	}
	public void setBpm(int bpm) {
		this.bpm = bpm;
	}
	public int getTrackNum() {
		return trackNum;
	}
	public void setTrackNum(int trackNum) {
		this.trackNum = trackNum;
	}
	public String getTitleImage() {
		return titleImage;
	}
	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}
	public String getSongImage() {
		return songImage;
	}
	public void setSongImage(String songImage) {
		this.songImage = songImage;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	
	
	public Track(int trackNum, String titleImage, String songImage, String songName, int bpm) {
		super();
		this.trackNum = trackNum;
		this.titleImage = titleImage;
		this.songImage = songImage;
		this.songName = songName;
		this.bpm = bpm;
	}
}
