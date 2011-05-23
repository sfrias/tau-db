package GUI.workers;

import tempenums.Tables;
import GUI.panels.Manage.cards.delete.DeleteCard;
import GUI.panels.Manage.cards.edit.EditSimpleCard;

public abstract class GenericEditDeleteWorker extends GenericWorker {
	protected Tables table;
	protected String[] fieldNames;
	protected String[] values ;
	protected int recordId;
	
	public GenericEditDeleteWorker(Tables table, String [] fieldNames, String [] fieldValue, int recordId, EditSimpleCard card) {
		super(card);
		this.table = table;
		this.fieldNames = fieldNames;
		this.values = fieldValue;
		this.recordId = recordId;
	}
	
	public GenericEditDeleteWorker(Tables table, int recordId, DeleteCard card){
		super(card);
		this.table = table;
		this.recordId = recordId;
	}

}
