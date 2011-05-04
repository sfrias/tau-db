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

public class GenericFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JLabel labelStatus;
	
	public GenericFrame() {
		super();
		GuiUtils.centerOnScreen(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(GuiUtils.defaultCloseWindowAdapter());
		
		IconButton buttonBack = GuiUtils.createActionButton("back", "back.png",
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								GuiHandler.switchFrames(new WelcomeScreenFrame());
							}
						});
					}
				});

		JButton buttonQuit = GuiUtils.createQuitButton();

		BlinkingStatusPanel panelStatus = GuiHandler.getStatusPanel();		
		/*JPanel panelStatus = new JPanel();
		Font font = new Font("Footlight MT Light", Font.BOLD, 18);
		JLabel labelTitle = new JLabel("Status:");
		labelTitle.setFont(font);
		labelStatus = new JLabel("Ready");
		labelStatus.setFont(font);
		panelStatus.setBorder(new EmptyBorder(5,0,0,0));
		panelStatus.add(labelTitle);
		panelStatus.add(labelStatus);*/
		
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
