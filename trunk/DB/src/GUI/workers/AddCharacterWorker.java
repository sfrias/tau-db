package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.panels.Manage.cards.add.AddCharacters;

public class AddCharacterWorker extends AddCharSuperWorker {

	public AddCharacterWorker(Tables table, String fieldName, String fieldValue, AddCharacters card) {
		super(table, fieldName, fieldValue, card);
	}

	@Override
    protected ResultHolder doInBackground() throws Exception {
            //TODO Hila: need to make sure we send (maybe if success already query the new and updated list)
            
            ExecutionResult exeRes = databaseManager.executeSimpleInsert(table, fieldName, value);  
            ResultHolder result = new ResultHolder(exeRes);
            return result;
    }

}
