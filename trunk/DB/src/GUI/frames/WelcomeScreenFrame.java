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

import GUI.GuiHandler;
import GUI.buttons.IconButton;
import GUI.commons.GuiUtils;
import GUI.dialogs.CustomDialog;
import enums.Frames;

public class WelcomeScreenFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private CustomDialog passwordDialog = new CustomDialog(this);
	private JLabel userMessage = new JLabel();

	public WelcomeScreenFrame(){
		buildFrame();
		pack();
		setResizable(false);
		GuiUtils.centerOnScreen(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(GuiUtils.defaultCloseWindowAdapter());
	}


	private void buildFrame(){
		setTitle("DB Project");
		setContentPane(mainPanelBuilder());
		//setSize(1000,1000);
	}


	private JPanel mainPanelBuilder(){
		JLabel labelProj = new JLabel(GuiUtils.readImageIcon("mainLogo.png"));
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
						GuiHandler.switchFrames(Frames.PlayFrame);
					}
				});

			}
		});
		IconButton buttonManage = GuiUtils.createActionButton("manage", "manage.png",new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						GuiHandler.switchFrames(Frames.ManageFrame);
					
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
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		panel.add(labelWelcome);
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		panel.add(labelQuestion);
		panel.add(Box.createRigidArea(new Dimension(0,15)));
		panel.add(actionButtons);
		panel.add(Box.createRigidArea(new Dimension(0,3)));
		
		JButton buttonEnableAdmin = GuiUtils.createActionButton("admin", "admin.png", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordDialog.setVisible(true);
				if (passwordDialog.isValidPassword()){
					GuiHandler.setAdmin(true);
				}
			}
		});
		buttonEnableAdmin.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(buttonEnableAdmin);		
		panel.add(Box.createRigidArea(new Dimension(0,8)));
		
		JButton buttonQuit = GuiUtils.createQuitButton();
		buttonQuit.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(buttonQuit);
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		
		userMessage.setAlignmentX(CENTER_ALIGNMENT);
		setUserMessage(GuiHandler.isAdmin());
		panel.add(userMessage);
		
		userMessage.setFont(new Font("Footlight MT Light", Font.PLAIN, 15));
		panel.setBorder(new EmptyBorder(20,20,10,20));
		return panel;

	}
	
	public void setUserMessage(boolean isAdmin){
		if (isAdmin){
			userMessage.setText("You are logged in as an Admin");
		} else{
			userMessage.setText("You are currently not logged in as an Admin");
		}
	}


}
