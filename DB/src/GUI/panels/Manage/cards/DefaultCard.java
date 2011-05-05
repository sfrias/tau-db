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
		
		JLabel arrowUp = new JLabel(GuiUtils.readImageIcon("arrowUpSmall.png"));
		add(BorderLayout.NORTH, arrowUp);
	
		JPanel panelText = new JPanel();

		JLabel text = new JLabel("choose a record");
		text.setFont(new Font("Footlight MT Light", Font.BOLD, 16));
		text.setAlignmentX(CENTER_ALIGNMENT);
		panelText.add(text);
		
		add(BorderLayout.CENTER, panelText);
		
	}
}
