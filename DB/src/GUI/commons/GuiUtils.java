package GUI.commons;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GUI.buttons.IconButton;
import GUI.frames.WelcomeScreenFrame;

public class GuiUtils {
    
	public static void centerOnScreen(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
            (screenSize.width - paneSize.width) / 2,
            (screenSize.height - paneSize.height) / 2);
    }
	
    public static JButton createIconButton(String icon) {
        JButton button = new JButton(readImageIcon(icon));
        button.setFocusPainted(true);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        return button;
    }

    public static ImageIcon readImageIcon(String fileName) {
        URL url = WelcomeScreenFrame.class.getResource("../images/" + fileName);
        if (url == null)
            return null;

        return new ImageIcon(java.awt.Toolkit.getDefaultToolkit().getImage(url));
    }
    
    public static void showQuitApplicationDialog(JFrame frame){
    	String message = "Are you sure you want to quit?";
    	String title = "Quit";
    	ImageIcon icon = readImageIcon("doorOpen.png");
    	int optionSelected = JOptionPane.showConfirmDialog(
    			frame, message, title, 
    			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
    	if (optionSelected==JOptionPane.YES_OPTION){
    		quit(frame);
    	}
    	
    }
	
	public static WindowAdapter defaultCloseWindowAdapter(JFrame fr){
		final JFrame frame = fr;
		return new WindowAdapter() {
            public void windowClosing(WindowEvent evt)
            {
            	GuiUtils.showQuitApplicationDialog(frame);  
            }
		};
	}
	
	public static ActionListener defaultQuitActionListener(JFrame fr){
		final JFrame frame = fr;
		return new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
						showQuitApplicationDialog(frame);
				}
		};
	}
	
	public static IconButton createActionButton(String text, String iconName,ActionListener actionListener){
		IconButton button = new IconButton(text, iconName);
		button.setPreferredSize(new Dimension(140, 40));
		button.setMaximumSize(new Dimension(140, 40));
		button.addActionListener(actionListener);
		
		return button;
		
	}
	
	public static JButton createQuitButton(JFrame frame){
		JButton button = GuiUtils.createActionButton("Quit","doorClosed.png",GuiUtils.defaultQuitActionListener(frame));
		button.addMouseListener(defaultQuitMouseListener(button));
		return button;
	}
	
	private static MouseListener defaultQuitMouseListener(JButton buttonQuit){
		final JButton button = buttonQuit;
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setIcon(GuiUtils.readImageIcon("doorOpen.png"));

			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setIcon(GuiUtils.readImageIcon("doorClosed.png"));

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		};
	}
	
	private static void quit(JFrame frame) {
	        frame.dispose();
	        System.exit(0);
	}

}
