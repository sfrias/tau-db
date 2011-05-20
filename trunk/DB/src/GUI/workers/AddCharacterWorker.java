package GUI.workers;

import Enums.ExecutionResult;
import GUI.commons.Pair;
import GUI.panels.Manage.cards.add.AddCharacters;

public class AddCharacterWorker extends GenericWorker {

	protected Pair[][] values;
	protected String [] tables ;
	
	public AddCharacterWorker(String [] tables, Pair[][] values, AddCharacters card) {
		super(card);
		this.tables = tables;
		this.values = values;
		
	}

	protected ResultHolder doInBackground() throws Exception {
            //TODO Hila: need to make sure we send (maybe if success already query the new and updated list)
            
            ExecutionResult exeRes = databaseManager.executeInsertCharacter(tables, values);  
            ResultHolder result = new ResultHolder(exeRes);
            return result;
    }

}
