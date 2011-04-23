package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import GUI.panels.Manage.cards.edit.EditGender;
import GUI.panels.Manage.cards.edit.EditName;

public class EditTab extends GenericTab{

	public EditTab(){
		super();
	}
	@Override
	public JPanel addCards(JPanel cards) {
		cards.add(new EditGender(), "GENDER");
		cards.add(new EditName(), "NAMES");
		return cards;
	}

	@Override
	public String getTabAction() {
		return "edit";
	}

}
