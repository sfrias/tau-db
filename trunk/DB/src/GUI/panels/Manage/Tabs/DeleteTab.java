package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import GUI.panels.Manage.cards.delete.DeleteGender;
import GUI.panels.Manage.cards.delete.DeleteName;

public class DeleteTab extends GenericTab{

	public DeleteTab(){
		super();
	}
	@Override
	public JPanel addCards(JPanel cards) {
		cards.add(new DeleteGender(), "GENDER");
		cards.add(new DeleteName(), "NAMES");
		return cards;
	}

	@Override
	public String getTabAction() {
		return "delete";
	}

}
