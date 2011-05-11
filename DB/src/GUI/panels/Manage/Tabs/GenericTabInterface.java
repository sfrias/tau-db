package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

public interface GenericTabInterface {
	
	public JPanel addCards(JPanel cards);
	
	//ADD, REMOVE, EDIT - show on action button and string
	public String getTabAction();

}
