package GUI.workers;

import GUI.commons.Pair;
import GUI.panels.Manage.cards.add.AddCharacters;
import dataTypes.ResultHolder;
import enums.ExecutionResult;

public class AddCharacterWorker extends GenericWorker {

	protected Pair[][] values;
	protected String [] tables ;
	
	public AddCharacterWorker(String [] tables, Pair[][] values, AddCharacters card) {
		super(card);
		this.tables = tables;
		this.values = values;
		
	}

	protected ResultHolder doInBackground(){
		
            ExecutionResult exeRes = databaseManager.executeInsertCharacter(tables, values);  
            ResultHolder result = new ResultHolder(exeRes);
            return result;
    }

}
