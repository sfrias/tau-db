package GUI.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import GUI.GuiHandler;
import GUI.buttons.IconButton;
import GUI.commons.GuiUtils;
import GUI.panels.General.BlinkingStatusPanel;
import enums.Frames;

public class GenericFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JLabel labelStatus;
	
	public GenericFrame() {
		super();
		GuiUtils.centerOnScreen(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(GuiUtils.defaultCloseWindowAdapter());
		
		IconButton buttonBack = GuiUtils.createActionButton("Main", "home.png",
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								GuiHandler.switchFrames(Frames.WelcomeScreenFrame);
							}
						});
					}
				});

		JButton buttonQuit = GuiUtils.createQuitButton();

		BlinkingStatusPanel panelStatus = GuiHandler.getStatusPanel();	
		
		JPanel panelButtom = new JPanel(new BorderLayout());
		panelButtom.add(BorderLayout.WEST, buttonBack);
		panelButtom.add(BorderLayout.EAST, buttonQuit);
		panelButtom.add(BorderLayout.CENTER,panelStatus);
		
		setLayout(new BorderLayout());
		add(BorderLayout.SOUTH,panelButtom);
	}
	
	protected void setStatus(String status){
		labelStatus.setText(status);
	}
	
}
