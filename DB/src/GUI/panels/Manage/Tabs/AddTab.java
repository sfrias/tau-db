package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import Enums.Tables;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.panels.Manage.cards.add.AddDisease;
import GUI.panels.Manage.cards.add.AddOccupation;
import GUI.panels.Manage.cards.add.AddOrganization;
import GUI.panels.Manage.cards.add.AddPower;
import GUI.panels.Manage.cards.add.AddSchool;
import GUI.panels.Manage.cards.add.AddUniverse;

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
	public JPanel addCards(JPanel cards){
		cards.add(new AddCharacters(), Tables.characters.toString().toUpperCase());
		cards.add(new AddDisease(), Tables.disease.toString().toUpperCase());
		cards.add(new AddOccupation(), Tables.occupation.toString().toUpperCase());
		cards.add(new AddOrganization(), Tables.organization.toString().toUpperCase());
		cards.add(new AddPower(), Tables.power.toString().toUpperCase());
		cards.add(new AddSchool(), Tables.school.toString().toUpperCase());
		cards.add(new AddUniverse(), Tables.universe.toString().toUpperCase());
		return cards;
	}

}
