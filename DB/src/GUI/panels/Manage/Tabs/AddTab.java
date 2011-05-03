package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import Utils.Tables;

import GUI.panels.Manage.cards.add.AddGender;
import GUI.panels.Manage.cards.add.AddOccupation;

public class AddTab extends GenericTab {
	private static final long serialVersionUID = 1L;

	public AddTab(){
		super();
	}
	
	@Override
	public String getTabAction() {
		return "add";
	}

	@Override
	public JPanel addCards(JPanel cards) {
		cards.add(new AddGender(), Tables.gender.toString().toUpperCase());
		cards.add(new AddOccupation(), Tables.occupation.toString().toUpperCase());
		return cards;
	}

}
