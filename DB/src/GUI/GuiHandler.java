package GUI;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Enums.Frames;
import GUI.commons.GuiUtils;
import GUI.frames.ManageFrame;
import GUI.frames.PlayFrame;
import GUI.frames.WelcomeScreenFrame;
import GUI.panels.CustomGlassPane;
import GUI.panels.General.BlinkingStatusPanel;


public class GuiHandler {

	private static BlinkingStatusPanel panelStatus = new BlinkingStatusPanel();  
	//private static CustomGlassPane glassPane = new CustomGlassPane();
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
	
	public static CustomGlassPane getGlassPane(){
		return glassPane;
	}

	public static void main(String[] args) {
		installLookAndFeel();
		WelcomeScreenFrame frame = new WelcomeScreenFrame();
		loadedFrame = frame;
		frame.setVisible(true);

	}

	public static void switchFrames(Frames frame){
		JFrame newFrame = null;
		switch (frame){
		case WelcomeScreenFrame:
			newFrame = new WelcomeScreenFrame();
			break;
		case ManageFrame:
			newFrame = new ManageFrame();
			break;
		case PlayFrame:
			newFrame = new PlayFrame();
			break;					
		}
		if (newFrame != null){
			newFrame.setVisible(true);
			GuiUtils.centerOnScreen(newFrame);
			loadedFrame.dispose();
			loadedFrame = newFrame;
		}
	}

	public static BlinkingStatusPanel getStatusPanel(){
		return panelStatus;
	}

	public static void startStatusFlash(){
		panelStatus.flash();
	//	glassPane.setVisible(true);
	}

	public static void stopStatusFlash(){
		panelStatus.clearFlashing();
	}
	
	public static boolean isStatusFlashing(){
		return panelStatus.isFlashing();
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
		showResultDialog(msg, title, icon);    		
	}

	//only during delete
	public static void showResultIntegrityDialog(){
		String msg = "This value is in use by some characters";
		String title = "Integrity Error";
		ImageIcon icon = GuiUtils.readImageIcon("thumbDown.png");
		showResultDialog(msg, title, icon);
	}

	public static void showResultExceptionDialog(String action){
		String msg = "An error occoured during " + action + " process";
		String title = "error";
		ImageIcon icon = GuiUtils.readImageIcon("thumbDown.png");
		showResultDialog(msg, title, icon);
	}
	
	public static void showCancelOperationDialog(){
		String msg = "Update operation aborted";
		String title = "Cancel";
		ImageIcon icon = GuiUtils.readImageIcon("thumbDown.png");
		showResultDialog(msg, title, icon);
	}

	public static void showUpdateCompleteDialog(){
		String msg = "Update completed Successfuly";
		String title = "done";
		ImageIcon icon = GuiUtils.readImageIcon("thumbUp.png");
		showResultDialog(msg, title, icon);
	}

	private static void showResultDialog(String msg, String title, ImageIcon icon){
		JOptionPane.showMessageDialog(loadedFrame, msg, title, JOptionPane.OK_OPTION, icon);       	    		
	}
	

	private static void quit() {
		loadedFrame.dispose();
		System.exit(0);
	}

	public static void showErrorGetRecords(){
		String msg = "could not retrieve records";
		String title = "error";
		ImageIcon icon = GuiUtils.readImageIcon("thumbDown.png");
		JOptionPane.showMessageDialog(loadedFrame, msg, title, JOptionPane.OK_OPTION,icon);
	}

}