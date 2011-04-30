package GUI.frames;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import GUI.commons.GuiUtils;
import GUI.panels.Manage.Tabs.AddTab;
import GUI.panels.Manage.Tabs.DeleteTab;
import GUI.panels.Manage.Tabs.EditTab;
import GUI.panels.Manage.Tabs.UpdateTab;

public class ManageFrame extends GenericFrame {
	private static final long serialVersionUID = 1L;
	
	JFrame frame = this;

	public ManageFrame(){
		super();
		buildFrame();
		GuiUtils.centerOnScreen(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(GuiUtils.defaultCloseWindowAdapter(frame));
	}
	
	private void buildFrame(){
		setTitle("Manage");
		setSize(600,600);
		add(BorderLayout.CENTER, tabPanelBuilder());
		//setContentPane(mainPanelBuilder());
		setVisible(true);
	}
	
/*	private JPanel mainPanelBuilder(){
		add(BorderLayout.CENTER, tabPanelBuilder());
		
		return panel;
	}*/
	
	private JTabbedPane tabPanelBuilder(){
		
		JTabbedPane tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("ADD", GuiUtils.readImageIcon("add.png"), new AddTab());
		tabbedPanel.addTab("EDIT", GuiUtils.readImageIcon("edit.png"), new EditTab());
		tabbedPanel.addTab("DELETE", GuiUtils.readImageIcon("remove.png"), new DeleteTab());
		tabbedPanel.addTab("UPDATE", GuiUtils.readImageIcon("update.png"), new UpdateTab());
		//tabbedPanel.setSelectedIndex(1);
		
		return tabbedPanel;
	}
	
}
