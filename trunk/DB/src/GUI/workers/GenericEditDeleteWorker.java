package GUI.workers;

import Enums.Tables;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;

public abstract class GenericEditDeleteWorker extends GenericWorker {
	protected Tables table;
	protected String[] fieldNames;
	protected String[] values ;
	protected int recordId;
	
	public GenericEditDeleteWorker(String action, Tables table, String [] fieldNames, String [] fieldValue, int recordId, 
			EditAndDeleteGenericCardPanel card) {
		super(action, card);
		this.table = table;
		this.fieldNames = fieldNames;
		this.values = fieldValue;
		this.recordId = recordId;
	}
	
	public GenericEditDeleteWorker(String action, Tables table, int recordId, EditAndDeleteGenericCardPanel card){
		super(action, card);
		this.table = table;
		this.recordId = recordId;
	}

}
