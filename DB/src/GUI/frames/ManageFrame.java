package GUI.frames;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import GUI.commons.GuiUtils;
import GUI.panels.Manage.Tabs.AddTab;
import GUI.panels.Manage.Tabs.DeleteTab;
import GUI.panels.Manage.Tabs.EditTab;
import GUI.panels.Manage.Tabs.UpdateTab;

public class ManageFrame extends GenericFrame {
	private static final long serialVersionUID = 1L;

	public ManageFrame() throws Exception{
		super();
		buildFrame();
	}

	private void buildFrame() throws Exception{
		setTitle("Manage");
		setSize(600,500);
		setResizable(false);
		add(BorderLayout.CENTER, tabPanelBuilder());
		//setContentPane(mainPanelBuilder());
	}

	/*	private JPanel mainPanelBuilder(){
		add(BorderLayout.CENTER, tabPanelBuilder());

		return panel;
	}*/

	private JTabbedPane tabPanelBuilder() throws Exception{

		JTabbedPane tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("ADD", GuiUtils.readImageIcon("add.png"), new AddTab());
		tabbedPanel.addTab("EDIT", GuiUtils.readImageIcon("edit.png"), new EditTab());
		tabbedPanel.addTab("DELETE", GuiUtils.readImageIcon("remove.png"), new DeleteTab());
		tabbedPanel.addTab("UPDATE", GuiUtils.readImageIcon("update.png"), new UpdateTab());
		//tabbedPanel.setSelectedIndex(1);

		return tabbedPanel;
	}
}
