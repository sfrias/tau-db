package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import GUI.panels.Manage.cards.add.AddGender;
import GUI.panels.Manage.cards.add.AddName;

public class UpdateTab extends GenericTab{
	public UpdateTab() {
		super();
	}

	@Override
	public JPanel addCards(JPanel cards) {
		cards.add(new AddGender(), "GENDER");
		cards.add(new AddName(), "NAMES");
		return cards;
	}

	@Override
	public String getTabAction() {
		// TODO Auto-generated method stub
		return null;
	}

}
