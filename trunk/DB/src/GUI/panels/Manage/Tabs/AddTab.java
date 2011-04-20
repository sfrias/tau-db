package GUI.panels.Manage.Tabs;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import GUI.panels.Manage.add.AddGender;
import GUI.panels.Manage.add.AddName;

public class AddTab extends GenericTab {

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
