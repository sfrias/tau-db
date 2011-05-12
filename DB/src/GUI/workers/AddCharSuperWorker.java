package GUI.workers;

import Enums.Tables;
import GUI.panels.Manage.cards.add.AddCharacters;

public abstract class AddCharSuperWorker extends GenericWorker {

	protected String fieldName;
	protected String value;
	protected Tables table ;
	
	public AddCharSuperWorker(Tables table, String fieldName, String fieldValue, AddCharacters card){
		super("add", card);
		this.fieldName = fieldName;
		this.value = fieldValue;
		this.table = table;
	}

}
