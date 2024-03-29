package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import enums.Tables;


import GUI.panels.Manage.cards.edit.EditCharacters;
import GUI.panels.Manage.cards.edit.EditDisease;
import GUI.panels.Manage.cards.edit.EditOccupation;
import GUI.panels.Manage.cards.edit.EditOrganization;
import GUI.panels.Manage.cards.edit.EditPlaceOfBirth;
import GUI.panels.Manage.cards.edit.EditPower;
import GUI.panels.Manage.cards.edit.EditSchool;
import GUI.panels.Manage.cards.edit.EditUniverse;

public class EditTab extends GenericTab{
	private static final long serialVersionUID = 1L;

	public EditTab(){
		super();
	}
	@Override
	public JPanel addCards(JPanel cards){
		cards.add(new EditCharacters(), Tables.characters.toString().toUpperCase());
		cards.add(new EditDisease(), Tables.disease.toString().toUpperCase());
		cards.add(new EditOccupation(), Tables.occupation.toString().toUpperCase());
		cards.add(new EditOrganization(), Tables.organization.toString().toUpperCase());
		cards.add(new EditPlaceOfBirth(), Tables.place_of_birth.toString().toUpperCase());
		cards.add(new EditPower(), Tables.power.toString().toUpperCase());
		cards.add(new EditSchool(), Tables.school.toString().toUpperCase());
		cards.add(new EditUniverse(), Tables.universe.toString().toUpperCase());

		return cards;
	}

	@Override
	public String getTabAction() {
		return "edit";
	}

}
