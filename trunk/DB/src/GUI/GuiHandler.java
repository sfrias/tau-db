package GUI;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import GUI.commons.GuiUtils;
import GUI.frames.ManageFrame;
import GUI.frames.PlayFrame;
import GUI.frames.WelcomeScreenFrame;
import GUI.panels.CustomGlassPane;
import GUI.panels.General.BlinkingStatusPanel;
import database.DatabaseManager;
import enums.Frames;


public class GuiHandler {

	private static BlinkingStatusPanel panelStatus = new BlinkingStatusPanel(); 
	private static CustomGlassPane glass ;
	private static JFrame loadedFrame = null;
	private static boolean isAdmin;
	private static DatabaseManager dbManager ;

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
		return glass;
	}
	
	public static void setGlassPane(CustomGlassPane glassPane){
		glass = glassPane;
	}
	
	public static JFrame getCurrentFrame(){
		return loadedFrame;
	}
	
	public static void setCurrentFrame(JFrame frame){
		loadedFrame = frame;
	}

	public static void main(String[] args) {
		installLookAndFeel();
		try {
			dbManager = new DatabaseManager();
			dbManager.initialize();
		} catch (Exception e) {
			showInitializeErrorDialog();
			e.printStackTrace();
			System.exit(0);
			
		}
		WelcomeScreenFrame frame = new WelcomeScreenFrame();
		setCurrentFrame(frame);
		setAdmin(false);
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
			setCurrentFrame(newFrame);
		}
	}

	public static BlinkingStatusPanel getStatusPanel(){
		return panelStatus;
	}

	public static void startStatusFlash(){
		panelStatus.flash();
		glass.setVisible(true);
	}

	public static void stopStatusFlash(){
		panelStatus.clearFlashing();
		glass.setVisible(false);
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
	
	public static void showCantHaveMoreThanOnePlaceOfBirthDialog(){
		String msg = "Can't have more than one place of birth";
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
	
	private static void showInitializeErrorDialog(){
		String msg = "Unabled to initialize the system\n The program will now quit";
		String title = "Error";
		ImageIcon icon = GuiUtils.readImageIcon("thumbDown.png");
		showResultDialog(msg, title, icon);
	}

	private static void showResultDialog(String msg, String title, ImageIcon icon){
		JOptionPane.showMessageDialog(loadedFrame, msg, title, JOptionPane.OK_OPTION, icon);       	    		
	}
	
	public static void showChooseCharactersDialog(){
		JOptionPane.showMessageDialog(loadedFrame, "Please select 2 characters to start the search", "info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showChooseFromComboDialog(){
		JOptionPane.showMessageDialog(loadedFrame, "You must choose from the given combobox. Free text is not allowed ", "info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showNoEmptyStringSearchDialog(){
		JOptionPane.showMessageDialog(loadedFrame, "Searching with empty string is not allowed", "info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showOnlyAsciiDialog(){
		JOptionPane.showMessageDialog(loadedFrame, "please use only ascii chars", "info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showAlgrithmResultDialog(boolean success, String title, String msg){
		ImageIcon icon;
		if (success){
			icon = GuiUtils.readImageIcon("thumbUp.png");
		}
		else{
			icon = GuiUtils.readImageIcon("thumbDown.png");
		}
		showResultDialog(msg, title, icon);
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
	
	public static void setAdmin(boolean shouldBeAdmin){
		isAdmin = shouldBeAdmin;
		setAdminMessage(isAdmin);
	}
	
	public static boolean isAdmin(){
		return isAdmin;
	}
	
	private static void setAdminMessage(boolean adminStatus){
		assert (loadedFrame instanceof WelcomeScreenFrame);
		
		WelcomeScreenFrame frame = (WelcomeScreenFrame)loadedFrame;
		frame.setUserMessage(adminStatus);
	}
	
	@SuppressWarnings("unchecked")
	public static void startCountDown(final SwingWorker worker){
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				worker.cancel(true);
				
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, 40000);
	}
	
	public static DatabaseManager getDatabaseManager(){
		return dbManager;
	}

}