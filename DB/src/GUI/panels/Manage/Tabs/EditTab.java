package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import GUI.panels.Manage.cards.edit.EditGender;
import GUI.panels.Manage.cards.edit.EditOccupation;

public class EditTab extends GenericTab{
	private static final long serialVersionUID = 1L;

	public EditTab(){
		super();
	}
	@Override
	public JPanel addCards(JPanel cards) {
		cards.add(new EditGender(), "GENDER");
		cards.add(new EditOccupation(), "OCCUPATION");
		return cards;
	}

	@Override
	public String getTabAction() {
		return "edit";
	}

}
