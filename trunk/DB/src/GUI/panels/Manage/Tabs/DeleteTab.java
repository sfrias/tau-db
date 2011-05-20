package GUI.panels.Manage.Tabs;

import javax.swing.JPanel;

import Enums.Tables;
import GUI.panels.Manage.cards.delete.DeleteCharacters;
import GUI.panels.Manage.cards.delete.DeleteCreator;
import GUI.panels.Manage.cards.delete.DeleteDisease;
import GUI.panels.Manage.cards.delete.DeleteEthnicity;
import GUI.panels.Manage.cards.delete.DeleteGender;
import GUI.panels.Manage.cards.delete.DeleteJob;
import GUI.panels.Manage.cards.delete.DeleteLocation;
import GUI.panels.Manage.cards.delete.DeleteOccupation;
import GUI.panels.Manage.cards.delete.DeleteOrganization;
import GUI.panels.Manage.cards.delete.DeletePower;
import GUI.panels.Manage.cards.delete.DeleteRank;
import GUI.panels.Manage.cards.delete.DeleteSchool;
import GUI.panels.Manage.cards.delete.DeleteSpecies;
import GUI.panels.Manage.cards.delete.DeleteUniverse;

public class DeleteTab extends GenericTab{
	private static final long serialVersionUID = 1L;

	public DeleteTab(){
		super();
	}
	@Override
	public JPanel addCards(JPanel cards){
		
		cards.add(new DeleteCharacters(), Tables.characters.toString().toUpperCase());
		cards.add(new DeleteCreator(), Tables.creator.toString().toUpperCase());
		cards.add(new DeleteDisease(), Tables.disease.toString().toUpperCase());
		cards.add(new DeleteEthnicity(), Tables.ethnicity.toString().toUpperCase());
		cards.add(new DeleteGender(), Tables.gender.toString().toUpperCase());
		cards.add(new DeleteJob(), Tables.job.toString().toUpperCase());
		cards.add(new DeleteLocation(), Tables.location.toString().toUpperCase());
		cards.add(new DeleteOccupation(), Tables.occupation.toString().toUpperCase());
		cards.add(new DeleteOrganization(), Tables.organization.toString().toUpperCase());
		cards.add(new DeletePower(), Tables.power.toString().toUpperCase());
		cards.add(new DeleteRank(), Tables.rank.toString().toUpperCase());
		cards.add(new DeleteSchool(), Tables.school.toString().toUpperCase());
		cards.add(new DeleteSpecies(), Tables.species.toString().toUpperCase());
		cards.add(new DeleteUniverse(), Tables.universe.toString().toUpperCase());
		return cards;
	}

	@Override
	public String getTabAction() {
		return "delete";
	}

}
