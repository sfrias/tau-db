package GUI.frames;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import GUI.GuiHandler;
import GUI.commons.GuiUtils;
import GUI.panels.CustomGlassPane;
import GUI.panels.Manage.Tabs.AddTab;
import GUI.panels.Manage.Tabs.DeleteTab;
import GUI.panels.Manage.Tabs.EditTab;
import GUI.panels.Manage.Tabs.UpdateTab;

public class ManageFrame extends GenericFrame {
	private static final long serialVersionUID = 1L;

	public ManageFrame(){
		super();
		
		CustomGlassPane glass = new CustomGlassPane(null, getContentPane());
		setGlassPane(glass);
		GuiHandler.setGlassPane(glass);
		
		buildFrame();
	}

	private void buildFrame(){
		setTitle("Manage");
		setSize(800,650);
		setResizable(false);
		add(BorderLayout.CENTER, tabPanelBuilder());
	}

	private JTabbedPane tabPanelBuilder(){

		JTabbedPane tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("ADD", GuiUtils.readImageIcon("add.png"), new AddTab());
		tabbedPanel.addTab("EDIT", GuiUtils.readImageIcon("edit.png"), new EditTab());
		tabbedPanel.addTab("DELETE", GuiUtils.readImageIcon("remove.png"), new DeleteTab());
		tabbedPanel.addTab("UPDATE", GuiUtils.readImageIcon("update.png"), new UpdateTab());

		return tabbedPanel;
	}
}
