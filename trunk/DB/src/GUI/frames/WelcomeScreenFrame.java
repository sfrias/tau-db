package GUI.frames;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import GUI.utilities.GuiUtils;

public class WelcomeScreenFrame extends JFrame {
	
	public WelcomeScreenFrame(){
		buildFrame();
		pack();
		setResizable(false);
		GuiUtils.centerOnScreen(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt)
                {
                        quit();
                }
        });
	}

	private void quit() {
	        dispose();
	        System.exit(0);
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
		
		JRadioButton buttonPlay = new JRadioButton("Play");
		buttonPlay.setSelected(true);
		JRadioButton buttonManage = new JRadioButton("Manage");
		ButtonGroup group = new ButtonGroup();
		group.add(buttonPlay);
		group.add(buttonManage);
		
		Box radioButtons = Box.createHorizontalBox();
		radioButtons.add(buttonPlay);
		radioButtons.add(buttonManage);
			
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(labelProj);
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(labelWelcome);
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(labelQuestion);
		panel.add(radioButtons);
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		
		JButton buttonNext = new JButton("Next");
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						//TODO open new frame
						
					}
				});
				
			}
		});
		buttonNext.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(buttonNext);
		
		panel.setBorder(new EmptyBorder(20,20,20,20));
		return panel;
		
	}

}
