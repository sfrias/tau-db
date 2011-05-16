package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.model.CharacterModel;
import GUI.panels.Manage.cards.add.AddCharacters;

public class AddCharacterAttributeWorker extends GenericWorker {
	
	protected String value;
	protected Tables table ;

	public AddCharacterAttributeWorker(Tables table, String fieldValue, AddCharacters card){
		super("add", card);
		this.value = fieldValue;
		this.table = table;
	}


	@Override
	protected ResultHolder doInBackground() throws Exception {
        
		ExecutionResult exeRes = databaseManager.executeSimpleInsert(table, table.toString()+"_name", value);
        
        if (exeRes.equals(ExecutionResult.Success_Simple_Add_Edit_Delete)){
        	exeRes = ExecutionResult.Success_Add_Character_Attribute;
    		CharacterModel model = new CharacterModel();
    		model.setAttributePairs(databaseManager.executeQueryAndGetValues(table, 3), table);
    		return new ResultHolder(model, exeRes);
        }
        return new ResultHolder(exeRes);
	}
}
