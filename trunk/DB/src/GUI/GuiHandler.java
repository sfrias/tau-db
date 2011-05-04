package GUI;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import GUI.commons.GuiUtils;
import GUI.frames.WelcomeScreenFrame;
import GUI.panels.General.BlinkingStatusPanel;


public class GuiHandler {

	private static BlinkingStatusPanel panelStatus = new BlinkingStatusPanel();   
	private static JFrame loadedFrame = null;
	
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
                loadedFrame = frame;
                frame.setVisible(true);
           
        }
        
        public static void switchFrames(JFrame frameToLoad){
			loadedFrame.dispose();
			frameToLoad.setVisible(true);
			GuiUtils.centerOnScreen(frameToLoad);
			loadedFrame=frameToLoad;
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
        
        public static void showQuitApplicationDialog(){
        	String message = "Are you sure you want to quit?";
        	String title = "Quit";
        	ImageIcon icon = GuiUtils.readImageIcon("doorOpen.png");
        	int optionSelected = JOptionPane.showConfirmDialog(
        			loadedFrame, message, title, 
        			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        	if (optionSelected==JOptionPane.YES_OPTION){
        		quit();
        	}
        	
        }
        
        public static void showResultSuccessDialog(String action){
        	String msg = "The " + action + " completed successfully";
        	String title = "Success";
        	ImageIcon icon = GuiUtils.readImageIcon("thumbUp.png");
        	ShowResultDialog(msg, title, icon);    		
        }
        
        //only during delete
        public static void ShowResultIntegrityDialog(){
        	String msg = "This value is in use by some characters";
        	String title = "Integrity Error";
        	ImageIcon icon = GuiUtils.readImageIcon("thumbDown.png");
        	ShowResultDialog(msg, title, icon);
        }
        
        public static void ShowResultExceptionDialog(String action){
        	String msg = "An error occoured during " + action + " process";
        	String title = "error";
        	ImageIcon icon = GuiUtils.readImageIcon("thumbDown.png");
        	ShowResultDialog(msg, title, icon);
        }
        
        private static void ShowResultDialog(String msg, String title, ImageIcon icon){
        	JOptionPane.showMessageDialog(loadedFrame, msg, title, JOptionPane.OK_OPTION, icon);       	    		
        }
        
    	private static void quit() {
	       loadedFrame.dispose();
	        System.exit(0);
	}	
      
}