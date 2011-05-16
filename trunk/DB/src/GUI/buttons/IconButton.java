package GUI.buttons;

import java.awt.Font;

import javax.swing.JButton;

import GUI.commons.GuiUtils;

public class IconButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	public IconButton(String text, String iconName){
		super();
		setIcon(GuiUtils.readImageIcon(iconName));
		setText(text.toUpperCase());
		setFont(new Font("Footlight MT Light",Font.BOLD,15));
		setIconTextGap(10);
		setBorderPainted(false);
		setSelected(false);
	}
}
