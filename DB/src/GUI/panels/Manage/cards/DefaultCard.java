package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.commons.GuiUtils;

public class DefaultCard extends JPanel {
	private static final long serialVersionUID = 1L;

	public DefaultCard(){
		super();
		setLayout(new BorderLayout());
		
		JLabel arrowUp = new JLabel(GuiUtils.readImageIcon("arrowUp.png"));
		add(BorderLayout.NORTH, arrowUp);
		
		JLabel text = new JLabel(" choose a table");
		text.setFont(new Font("Footlight MT Light", Font.BOLD, 72));
		text.setAlignmentX(CENTER_ALIGNMENT);
		add(BorderLayout.CENTER, text);
		
	}
}
