package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import Enums.Tables;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.panels.Manage.cards.add.AddCreator;
import GUI.panels.Manage.cards.add.AddDisease;
import GUI.panels.Manage.cards.add.AddEthnicity;
import GUI.panels.Manage.cards.add.AddGender;
import GUI.panels.Manage.cards.add.AddJob;
import GUI.panels.Manage.cards.add.AddLocation;
import GUI.panels.Manage.cards.add.AddOccupation;
import GUI.panels.Manage.cards.add.AddOrganization;
import GUI.panels.Manage.cards.add.AddPower;
import GUI.panels.Manage.cards.add.AddRank;
import GUI.panels.Manage.cards.add.AddSchool;
import GUI.panels.Manage.cards.add.AddSpecies;
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
		cards.add(new AddCreator(), Tables.creator.toString().toUpperCase());
		cards.add(new AddDisease(), Tables.disease.toString().toUpperCase());
		cards.add(new AddEthnicity(), Tables.ethnicity.toString().toUpperCase());
		cards.add(new AddGender(), Tables.gender.toString().toUpperCase());
		cards.add(new AddJob(), Tables.job.toString().toUpperCase());
		cards.add(new AddLocation(), Tables.location.toString().toUpperCase());
		cards.add(new AddOccupation(), Tables.occupation.toString().toUpperCase());
		cards.add(new AddOrganization(), Tables.organization.toString().toUpperCase());
		cards.add(new AddPower(), Tables.power.toString().toUpperCase());
		cards.add(new AddRank(), Tables.rank.toString().toUpperCase());
		cards.add(new AddSchool(), Tables.school.toString().toUpperCase());
		cards.add(new AddSpecies(), Tables.species.toString().toUpperCase());
		cards.add(new AddUniverse(), Tables.universe.toString().toUpperCase());
		return cards;
	}

}
