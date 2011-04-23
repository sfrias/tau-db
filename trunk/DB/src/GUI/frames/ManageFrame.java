package GUI.frames;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import GUI.panels.Manage.Tabs.AddTab;
import GUI.panels.Manage.Tabs.DeleteTab;
import GUI.panels.Manage.Tabs.EditTab;
import GUI.panels.Manage.Tabs.UpdateTab;
import GUI.utilities.GuiUtils;

public class ManageFrame extends JFrame {
	

	public ManageFrame(){
		buildFrame();
		GuiUtils.centerOnScreen(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt)
                {
                        backOperation();
                }
        });
	}
	
	private void buildFrame(){
		setTitle("Manage");
		setSize(600,600);
		setContentPane(tabPanelBuilder());
		setVisible(true);
	}
	
	
	private void backOperation(){
		WelcomeScreenFrame frame = new WelcomeScreenFrame();
		frame.setVisible(true);
		dispose();
	}
	
	private JTabbedPane tabPanelBuilder(){
		
		JTabbedPane tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("ADD", new AddTab());
		tabbedPanel.addTab("EDIT", new EditTab());
		tabbedPanel.addTab("DELETE", new DeleteTab());
		tabbedPanel.addTab("UPDATE", new UpdateTab());
		
		return tabbedPanel;
	}
	
}
