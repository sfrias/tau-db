package GUI.workers;

import dataTypes.ResultHolder;
import GUI.commons.Pair;
import GUI.model.CharacterModel;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.panels.Manage.cards.edit.EditCharacters;
import enums.ExecutionResult;
import enums.Tables;

public class AddCharacterAttributeWorker extends GenericWorker {
	
	protected String value;
	protected Tables table ;

	public AddCharacterAttributeWorker(Tables table, String fieldValue, AddCharacters card){
		super(card);
		this.value = fieldValue;
		this.table = table;
	}

	public AddCharacterAttributeWorker(Tables table, String fieldValue, EditCharacters card){
		super(Action.ADD, card);
		this.value = fieldValue;
		this.table = table;
	}

	@Override
	protected ResultHolder doInBackground() {
        
		ExecutionResult exeRes = databaseManager.executeSimpleInsert(table, table.name()+"_name", value);
        
        if (exeRes.equals(ExecutionResult.Success_Simple_Add_Edit_Delete)){
        	exeRes = ExecutionResult.Success_Add_Character_Attribute;

        	CharacterModel model = new CharacterModel();
    		Pair [] values = databaseManager.executeQueryAndGetValues(table);
    		if (values != null){
        		model.setAttributePairs(values, table);
        		return new ResultHolder(model, exeRes);
    		}
        }
        return new ResultHolder(exeRes);
	}
}
