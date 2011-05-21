package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.model.CharacterModel;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.panels.Manage.cards.edit.EditCharacters;

public class GetAllAttributesWorker extends GenericWorker{
	
	public GetAllAttributesWorker(AddCharacters parent) {
		super(parent);
	}
	
	public GetAllAttributesWorker(EditCharacters parent) {
		super(Action.QUERY, parent);
	}
	
	
	@Override
	protected ResultHolder doInBackground(){	
		//TODO add exceptions
		CharacterModel model = new CharacterModel();
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.creator), Tables.creator);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.disease), Tables.disease);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.ethnicity),Tables.ethnicity);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.gender),Tables.gender);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.job),Tables.job);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.occupation),Tables.occupation);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.organization),Tables.organization);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.power),Tables.power);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.rank),Tables.rank);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.school),Tables.school);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.species),Tables.species);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.universe),Tables.universe);
		
		ResultHolder result = new ResultHolder(model, ExecutionResult.Success_Characters_Query);
		return result;
	}

}
