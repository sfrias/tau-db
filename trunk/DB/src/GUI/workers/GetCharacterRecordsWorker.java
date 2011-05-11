package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.model.CharacterModel;
import GUI.panels.Manage.cards.add.AddCharacters;

public class GetCharacterRecordsWorker extends GenericWorker{
	
	public GetCharacterRecordsWorker(AddCharacters parent) {
		super(parent);
	}
	
	
	@Override
	protected ResultHolder doInBackground(){	
		//TODO add exceptions
		CharacterModel model = new CharacterModel();
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.creator, 3), Tables.creator);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.disease, 3), Tables.disease);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.ethnicity, 3),Tables.ethnicity);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.gender, 3),Tables.gender);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.job, 3),Tables.job);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.location, 3),Tables.location);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.occupation, 3),Tables.occupation);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.organization, 3),Tables.organization);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.power, 3),Tables.power);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.rank, 3),Tables.rank);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.school, 3),Tables.school);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.species, 3),Tables.species);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.universe, 3),Tables.universe);
		
		ResultHolder result = new ResultHolder(model, ExecutionResult.Success_Characters_Query);
		return result;
	}

}
