package GUI.buttons;

import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import GUI.frames.WelcomeScreenFrame;

public class IconButton extends JButton {
	
	
	private static ImageIcon createIcon(String iconName){
		URL imgURL = WelcomeScreenFrame.class.getResource("../images/" + iconName);
		if (imgURL != null) {
	        return new ImageIcon(imgURL, iconName);
	    } else {
	        System.err.println("Couldn't find file: " + iconName);
	        return null;
	    }
	}
	
	public IconButton(String text, String iconName){
		super();
		setIcon(createIcon(iconName));
		setText(text.toUpperCase());
		setFont(new Font("Footlight MT Light",Font.BOLD,15));
		setIconTextGap(10);
		setBorderPainted(false);
		setSelected(false);
	}
}
