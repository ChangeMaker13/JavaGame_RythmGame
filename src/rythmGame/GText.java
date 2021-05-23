package rythmGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GText extends GameObject{
	
	private String str;
	private int x, y;

	private String font;
	private int style;
	private int size;
	private Color color;

	public GText(String _str, int xpos, int ypos,
			String _font, int _style, int _size, Color _color) {
		super(Type.TEXT);
		// TODO Auto-generated constructor stub
		str = _str; x = xpos; y = ypos;
		font = _font; style = _style; size = _size; color = _color;
	}
	
	public void Render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);
		g.setFont(new Font(font, style, size));
		g.drawString(str, x, y);
	};
	
	public void ResetStr(String str) {
		this.str = str;
	}
}
