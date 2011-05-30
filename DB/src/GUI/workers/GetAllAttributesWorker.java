package GUI.workers;

import GUI.model.CharacterModel;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.panels.Manage.cards.edit.EditCharacters;
import dataTypes.ResultHolder;
import enums.ExecutionResult;
import enums.Tables;

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
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.disease), Tables.disease);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.occupation),Tables.occupation);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.organization),Tables.organization);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.place_of_birth),Tables.place_of_birth);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.power),Tables.power);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.school),Tables.school);
		model.setAttributePairs(databaseManager.executeQueryAndGetValues(Tables.universe),Tables.universe);
		
		ResultHolder result = new ResultHolder(model, ExecutionResult.Success_Characters_Query);
		return result;
	}

}
