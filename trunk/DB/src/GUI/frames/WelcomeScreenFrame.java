package GUI.frames;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import GUI.buttons.IconButton;
import GUI.commons.GuiUtils;

public class WelcomeScreenFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JButton buttonQuit ;
	private JFrame frame = this;
	
	public WelcomeScreenFrame(){
		buildFrame();
		pack();
		setResizable(false);
		GuiUtils.centerOnScreen(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(GuiUtils.defaultCloseWindowAdapter(frame));
	}

	
	private void buildFrame(){
		setTitle("DB Project");
		setContentPane(mainPanelBuilder());
		//setSize(1000,1000);
	}
	

	private JPanel mainPanelBuilder(){
		JLabel labelProj = new JLabel("DB Project");
		labelProj.setFont(new Font("Footlight MT Light", Font.BOLD, 72));
		labelProj.setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel labelWelcome = new JLabel("Welcome");
		labelWelcome.setFont(new Font("Footlight MT Light", Font.BOLD, 16));
		labelWelcome.setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel labelQuestion = new JLabel("What would you like to do ?");
		labelQuestion.setFont(new Font("Footlight MT Light", Font.BOLD, 16));
		labelQuestion.setAlignmentX(CENTER_ALIGNMENT);
		
		IconButton buttonPlay = GuiUtils.createActionButton("play", "play.png", new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JFrame frame = new PlayFrame();
							frame.setVisible(true);
							dispose();
						}
					});
					
				}
		});
		IconButton buttonManage = GuiUtils.createActionButton("manage", "manage.png",new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JFrame frame = new ManageFrame();
							frame.setVisible(true);
							dispose();
						}
					});
					
				}
		});
		
		Box actionButtons = Box.createHorizontalBox();
		actionButtons.add(buttonPlay);
		actionButtons.add(Box.createRigidArea(new Dimension(10, 0)));
		actionButtons.add(buttonManage);
			
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(labelProj);
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(labelWelcome);
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(labelQuestion);
		panel.add(Box.createRigidArea(new Dimension(0,15)));
		panel.add(actionButtons);
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		
		buttonQuit = GuiUtils.createQuitButton(frame);
		buttonQuit.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(buttonQuit);
		
		panel.setBorder(new EmptyBorder(20,20,20,20));
		return panel;
		
	}

	
}
