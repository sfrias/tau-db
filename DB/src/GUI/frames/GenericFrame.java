package GUI.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import GUI.buttons.IconButton;
import GUI.commons.GuiUtils;

public class GenericFrame extends JFrame {
	public GenericFrame() {
		super();
		IconButton buttonBack = GuiUtils.createActionButton("back", "back.png",
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								WelcomeScreenFrame frame = new WelcomeScreenFrame();
								frame.setVisible(true);
								dispose();
							}
						});
					}
				});

		JButton buttonQuit = GuiUtils.createQuitButton(this);

		JPanel panelButtom = new JPanel(new BorderLayout());
		panelButtom.add(BorderLayout.WEST, buttonBack);
		panelButtom.add(BorderLayout.EAST, buttonQuit);
		
		setLayout(new BorderLayout());
		add(BorderLayout.SOUTH,panelButtom);
	}
	
}
