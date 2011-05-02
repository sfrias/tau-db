package GUI;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import GUI.frames.WelcomeScreenFrame;
import GUI.panels.General.BlinkingStatusPanel;


public class GuiBuilder {

	private static BlinkingStatusPanel panelStatus = new BlinkingStatusPanel();    
	
	private static void installLookAndFeel() {
                try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        
        public static void main(String[] args) {
                installLookAndFeel();
                WelcomeScreenFrame frame = new WelcomeScreenFrame();
                frame.setVisible(true);
        }
        
        public static BlinkingStatusPanel getStatusPanel(){
        	return panelStatus;
        }
        
        public static void startStatusFlash(){
        	panelStatus.flash();
        }
        
        public static void stopStatusFlash(){
        	panelStatus.clearFlashing();
        }
      
}