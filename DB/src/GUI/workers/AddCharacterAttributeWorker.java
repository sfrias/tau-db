package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.model.CharacterModel;
import GUI.panels.Manage.cards.add.AddCharacters;

public class AddCharacterAttributeWorker extends AddCharSuperWorker {

	public AddCharacterAttributeWorker(Tables table, String fieldName, String fieldValue, AddCharacters card) {
		super(table, fieldName, fieldValue, card);
	}

	@Override
	protected ResultHolder doInBackground() throws Exception {
        
		ExecutionResult exeRes = databaseManager.executeSimpleInsert(table, fieldName, value);
        
        if (exeRes.equals(ExecutionResult.Success_Simple_Add_Edit_Delete)){
        	exeRes = ExecutionResult.Success_Add_Character_Attribute;
    		CharacterModel model = new CharacterModel();
    		model.setAttributePairs(databaseManager.executeQueryAndGetValues(table, 3), table);
    		return new ResultHolder(model, exeRes);
        }
        return new ResultHolder(exeRes);
	}
}
