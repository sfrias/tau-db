package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import GUI.panels.Manage.cards.add.AddGender;
import GUI.panels.Manage.cards.add.AddName;

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
		cards.add(new AddGender(), "GENDER");
		cards.add(new AddName(), "NAMES");
		return cards;
	}

}
