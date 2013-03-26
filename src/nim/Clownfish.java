// Clownfish.java
// Code modifications made by : Drew Murphy
package nim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Clownfish
{
	final static int OBJECT_WIDTH = 60;
	final static int OBJECT_HEIGHT = 40;
	static private int nSelected;
	

	private boolean dropped, selected;
	private int ox, oy;

	Clownfish (int ox, int oy) {
		setOrigin (ox, oy);
	}

	boolean contains (int x, int y) {
		return (x >= ox && x <= ox+OBJECT_WIDTH &&
				y >= oy && y <= oy+OBJECT_HEIGHT);
	}

	void draw (Graphics g) {
		java.net.URL fishURL = this.getClass().getResource("/images/fish.png");
		java.net.URL glowfishURL = this.getClass().getResource("/images/glowfish.png");
		
		Image fish = Toolkit.getDefaultToolkit().getImage(fishURL);
		Image glowfish = Toolkit.getDefaultToolkit().getImage(glowfishURL);
		
		if (selected == false)
			g.drawImage(fish, ox, oy, null);
		else if (selected == true)
			g.drawImage(glowfish, ox, oy, null);
	}

	int getOriginX () {
		return ox;
	}

	int getOriginY () {
		return oy;
	}

	boolean isDropped () {
		return dropped;
	}

	boolean isSelected () {
		return selected;
	}

	void setDropped (boolean dropped) {
		this.dropped = dropped;
	}

	void setOrigin (int x, int y) {
		ox = x;
		oy = y;
	}

	void setSelected (boolean selected) {
		if (!this.selected && selected == true)
			nSelected++;

		if (this.selected && selected == false)
			nSelected--;

		this.selected = selected;
	}
	
	static int getNumSelected() {
		return nSelected;
	}
}
