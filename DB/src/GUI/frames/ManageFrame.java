package GUI.frames;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.commons.GuiUtils;
import GUI.panels.Manage.Tabs.AddTab;
import GUI.panels.Manage.Tabs.DeleteTab;
import GUI.panels.Manage.Tabs.EditTab;
import GUI.panels.Manage.Tabs.GenericTab;
import GUI.panels.Manage.Tabs.UpdateTab;

public class ManageFrame extends GenericFrame {
	private static final long serialVersionUID = 1L;

	public ManageFrame(){
		super();
		buildFrame();
	}

	private void buildFrame(){
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

	private JTabbedPane tabPanelBuilder(){

		JTabbedPane tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("ADD", GuiUtils.readImageIcon("add.png"), new AddTab());
		tabbedPanel.addTab("EDIT", GuiUtils.readImageIcon("edit.png"), new EditTab());
		tabbedPanel.addTab("DELETE", GuiUtils.readImageIcon("remove.png"), new DeleteTab());
		tabbedPanel.addTab("UPDATE", GuiUtils.readImageIcon("update.png"), new UpdateTab());
		tabbedPanel.addChangeListener(new TabChangedListerner()); 
		//tabbedPanel.setSelectedIndex(1);

		return tabbedPanel;
	}

	private class TabChangedListerner implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent changeEvent) {
			JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
			int index = sourceTabbedPane.getSelectedIndex();
			GenericTab genericTab = (GenericTab) sourceTabbedPane.getTabComponentAt(index);
		}

	}
}
