package nim;
// GuiNim.java
// Code modifications made by : Drew Murphy

import java.awt.Toolkit;

import javax.swing.*;

public class GuiNim extends JFrame {
   public GuiNim (String title) {
      // Display title in frame window's title bar.
	   super (title);
	   setDefaultCloseOperation (EXIT_ON_CLOSE);
	   
	   java.net.URL logoURL = this.getClass().getResource("/images/fish.png");
	   setIconImage(Toolkit.getDefaultToolkit().getImage(logoURL));
	   
	   try {
		      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		    } catch (InstantiationException e) {
		    } catch (ClassNotFoundException e) {
		    } catch (UnsupportedLookAndFeelException e) {
		    } catch (IllegalAccessException e) {
		    }
	   
	   getContentPane ().add (new NimPanel (this));
	   pack (); // Pack all components to their preferred sizes.
	   setResizable (false);
	   // Display frame window and all contained components.
	   setVisible (true);
   }

   
   public static void main(String[] args) {
		// Schedule App's GUI create & show for event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GuiNim ("Finding Nimo");
			}
		});
	}
}

