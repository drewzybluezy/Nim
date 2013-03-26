// NimPanel.java
// Code modifications made by : Drew Murphy

package nim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class NimPanel extends JPanel  {
	private static int NFISH;
	private Clownfish [] clownfish;
	
	private int height, width;
	private Dimension preferredSize = new Dimension (800, 600);
	
	private int prev = -1;
	
	private ImageIcon logo;
	private int logow, logoh;
	
	private ImageIcon pile;
	private int pilew, pileh;
	
	private JFrame enclosingFrame;
	
	private ImageIcon pileNeg;

	private final static int ANIM_DELAY = 100;
	private final static File DROP_SOUND = new File ("sounds/drop.wav");

	NimModel model;

	public NimPanel (JFrame enclosingFrame) {
		roll();
		model = new NimModel(NFISH);
		model.newGame();
		
		this.enclosingFrame = enclosingFrame;
		setLayout(new BorderLayout());
		buildGUI();

	}


	private MouseAdapter makeMouseListener() {
		MouseAdapter ma;
		ma = new MouseAdapter () {
			
			//MOUSE PRESSED
			public void mousePressed (MouseEvent e) {
				
				int x = e.getX ();
				int y = e.getY ();
				
				for (int i = 0; i < NFISH; i++)
					if (clownfish [i].contains (x, y) &&
							!clownfish [i].isDropped ()) {
						
						if (clownfish[i].isSelected()) {
							clownfish[i].setSelected(false);
							repaint();
							return;
						}
						
						if (Clownfish.getNumSelected() == 3)
							return;
						
						clownfish[i].setSelected(true);
						
						reorder ();
						repaint ();
						break;
					}
			}

		};
		return ma;
	}
	
	private void ready() {
		
		//  PLAYER TAKES TURN
		if (Clownfish.getNumSelected() == 0)
			return;
		
		model.playTurn(Clownfish.getNumSelected());
		for (int i = 0; i < NFISH; i++)
			if (clownfish [i].isSelected ()) {
				clownfish [i].setSelected (false);
				clownfish [i].setDropped (true);
			}
		
		repaint ();
		playSound (DROP_SOUND);
		
		// Player Game Over condition
		if (model.gameOver()) {
			gameOver("Computer player wins. Play again?");
		}
		
		//  COMPUTER TAKES TURN
		
		int takenFish = model.takeBestMove();
		for (int i = 0; i < NFISH; i++)
			if (!clownfish [i].isDropped ()) {
				clownfish [i].setDropped (true);
				takenFish--;
				if (takenFish == 0)
					break;
			}
		repaint ();

		// Computer Game Over Condition
		if (model.gameOver()) {
			gameOver("Human player wins. Play again?");
		}
		
	}

	public void buildGUI() {
		logo = new ImageIcon ("images/logo.png");
		logow = logo.getImage ().getWidth (this);
		logoh = logo.getImage ().getHeight (this);
		
		pile = new ImageIcon ("images/pile.png");
		pilew = pile.getImage ().getWidth (this);
		pileh = pile.getImage ().getHeight (this);

		createNegative();
		
		width = preferredSize.width;
		height = preferredSize.height;
		
		
		JButton ready = new JButton("Ready");
		
		ready.setForeground(Color.BLUE);
		try {
			Font niagara = Font.createFont(Font.TRUETYPE_FONT,
					this.getClass().getResourceAsStream("/images/niagara.TTF"));
			Font font = niagara.deriveFont((float)36);
			ready.setFont(font);
		} catch (FontFormatException e) {} catch (IOException e) {}
		
		ready.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ready();
			}
		});
		add(ready, BorderLayout.PAGE_END);
		
		spawnFish();
		
		//Press and release events
		MouseAdapter ma = makeMouseListener();
		addMouseListener(ma);
		
		repaint();
	}


	private boolean continueGame (String message) {
		if (JOptionPane.showConfirmDialog (enclosingFrame,
				message,
				"Finding Nimo",
				JOptionPane.YES_NO_OPTION) ==
					JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	public Dimension getPreferredSize () {
		return preferredSize;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent (g);

		java.net.URL backURL = this.getClass().getResource("/images/back.png");
		java.net.URL fishURL = this.getClass().getResource("/images/fish.png");
		
		Image fish = Toolkit.getDefaultToolkit().getImage(fishURL);
		Image background = Toolkit.getDefaultToolkit().getImage(backURL);
		
		g.setColor (Color.white);
		g.setFont (new Font ("Arial", Font.BOLD, 14));
		
		try {
			Font niagara = Font.createFont(Font.TRUETYPE_FONT,
					this.getClass().getResourceAsStream("/images/niagara.TTF"));
			Font font = niagara.deriveFont((float)36);
			g.setFont(font);
		} catch (FontFormatException e) {} catch (IOException e) {}
		
		g.drawImage(background, 0, 0, null);
		FontMetrics fm = g.getFontMetrics ();

		// Draw centered labels.
		String s = "Computer Player's Pile";
		g.drawString (s, (pilew-fm.stringWidth (s))/2, 150);

		s = "Human Player's Pile";
		g.drawString (s, getWidth ()-pilew+(pilew-fm.stringWidth (s))/2, 150);
		
		s = model.getNumFish() + "";
		g.drawString(s, (getWidth()/2), (getHeight()/2) - 25);

		// Draw match-pile images.
		g.drawImage (pile.getImage(), 0, 150, this);
		g.drawImage(fish, pilew/2 - 65, 175+(pileh/2) - 30, this);
		
		g.drawImage (pile.getImage(), width-pilew, 150, this);
		g.drawImage(fish, width-pilew+(pilew/2) - 65, 175+(pileh/2) - 30, this);
		
		s = model.getNumCom() + "";
		g.drawString(s, pilew/2, 175+(pileh/2));
		
		s = model.getNumHuman() + "";
		g.drawString(s, width-pilew+(pilew/2), 175+(pileh/2));

		// Draw logo image.
		g.drawImage (logo.getImage(), (width-logow)/2, 10, this);

		// Draw all non-dropped fish
		for (int i = NFISH-1; i >= 0; i--)
			if (!clownfish [i].isDropped ())
				clownfish [i].draw (g);
	}

	private void playSound (File file) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream (file);
			AudioFormat af = ais.getFormat ();
			
			DataLine.Info dli = new DataLine.Info (SourceDataLine.class, af);
			
			if (AudioSystem.isLineSupported (dli)) {
				
				SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine (dli);
				sdl.open (af);
				sdl.start ();
				
				int frameSize = af.getFrameSize ();
				int bufferLenInFrames = sdl.getBufferSize () / 8;
				int bufferLenInBytes = bufferLenInFrames * frameSize;
				
				byte [] buffer = new byte [bufferLenInBytes];
				
				int numBytesRead;
				while ((numBytesRead = ais.read (buffer)) != -1)
					sdl.write (buffer, 0, numBytesRead);
			}
		}
		catch (LineUnavailableException e) {
		}
		catch (UnsupportedAudioFileException e) {
		}
		catch (IOException e) {
		}
	}

	private void reorder () {
		// Compute indexes of selected Matches.
		int [] indexes = { -1, -1, -1 };
		int j = 0;

		for (int i = 0; i < NFISH; i++)
			if (clownfish [i].isSelected ())
				indexes [j++] = i;

		// Shuffle Matches array so that selected Clownfish objects appear at the
		// beginning.
		Clownfish temp = clownfish [0];
		clownfish [0] = clownfish [indexes [0]];
		clownfish [indexes [0]] = temp;

		if (indexes [1] != -1) {
			temp = clownfish [1];
			clownfish [1] = clownfish [indexes [1]];
			clownfish [indexes [1]] = temp;
		}

		if (indexes [2] != -1) {
			temp = clownfish [2];
			clownfish [2] = clownfish [indexes [2]];
			clownfish [indexes [2]] = temp;
		}

		// Update prev to account for shuffling.
		if (prev == indexes [0])
			prev = 0;
		else
			if (prev == indexes [1])
				prev = 1;
			else
				prev = 2;
	}
	
	private void spawnFish() {
		clownfish = new Clownfish [NFISH];
		for (int i = 0; i < NFISH; i++)
			clownfish [i] = new Clownfish ((width-(Clownfish.OBJECT_WIDTH*5+15))/2+
					i%5*(Clownfish.OBJECT_WIDTH+5),
					330+ i/5*(Clownfish.OBJECT_HEIGHT+5));
	}

	private void resetGUIforNewGame () {
		clownfish = null;
		roll();
		spawnFish();
		model.rebuild(NFISH);
	}
	
	public void roll() {
		Random rand = new Random();
		NFISH = rand.nextInt(10) + 10;
	}
	
	private void createNegative() {
		int [] pixels = new int [pilew*pileh];

		java.awt.image.PixelGrabber pg;
		pg = new java.awt.image.PixelGrabber (pile.getImage (), 0, 0, pilew,
				pileh, pixels, 0, pilew);

		try {
			pg.grabPixels ();
		}
		catch (InterruptedException e) {
		}

		for (int i = 0; i < pixels.length; i++)
			pixels [i] = pixels [i] ^ 0xffffff;

		java.awt.image.MemoryImageSource mis;
		mis = new java.awt.image.MemoryImageSource (pilew, pileh, pixels, 0,
				pilew);
		pileNeg = new ImageIcon (createImage (mis));
	}
	
	private void gameOver(String s) {
		final ImageIcon oldPile = pile;

		ActionListener al;
		al = new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				repaint ();

				if (pile == oldPile)
					pile = pileNeg;
				else
					pile = oldPile;
			}
		};

		Timer t = new Timer (ANIM_DELAY, al);
		t.start ();

		boolean continuePlay;
		continuePlay = continueGame(s);

		t.stop ();
		pile = oldPile;
		repaint ();

		if (!continuePlay)
			System.exit (0);

		resetGUIforNewGame ();
		model.newGame();
	}
}



