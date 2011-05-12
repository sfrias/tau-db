package GUI.commons;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import GUI.GuiHandler;
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
    
    public static ImageIcon readImageIcon(String fileName, int width, int height){
    	ImageIcon icon = readImageIcon(fileName);
    	Image img = icon.getImage();
    	Image scaledImg = img.getScaledInstance(width, height,Image.SCALE_DEFAULT);
    	return new ImageIcon(scaledImg);
    }
    	
	public static WindowAdapter defaultCloseWindowAdapter(){
		return new WindowAdapter() {
            public void windowClosing(WindowEvent evt)
            {
            	GuiHandler.showQuitApplicationDialog();  
            }
		};
	}
	
	public static ActionListener defaultQuitActionListener(){
		return new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
						GuiHandler.showQuitApplicationDialog();
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
	
	public static JButton createQuitButton(){
		JButton button = GuiUtils.createActionButton("Quit","doorClosed.png",GuiUtils.defaultQuitActionListener());
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
	
}