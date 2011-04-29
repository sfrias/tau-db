package GUI.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

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
        button.setContentAreaFilled(false);
        return button;
    }

    public static ImageIcon readImageIcon(String fileName) {
        URL url = WelcomeScreenFrame.class.getResource("../images/" + fileName);
        if (url == null)
            return null;

        return new ImageIcon(java.awt.Toolkit.getDefaultToolkit().getImage(url));
    }



}
