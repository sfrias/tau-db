package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import GUI.panels.Manage.cards.add.AddGender;
import GUI.panels.Manage.cards.add.AddOccupation;

public class UpdateTab extends GenericTab{
	private static final long serialVersionUID = 1L;
	
	public UpdateTab(){
		super();
	}

	@Override
	public JPanel addCards(JPanel cards) {
		cards.add(new AddGender(), "GENDER");
		cards.add(new AddOccupation(), "NAMES");
		return cards;
	}

	@Override
	public String getTabAction() {
		// TODO Auto-generated method stub
		return null;
	}

}
