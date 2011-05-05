package GUI.panels.Manage.cards.delete;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import GUI.workers.GenericWorker;

public class DeleteCharacters extends DeleteCard{

	private static final long serialVersionUID = 1L;
	
	public DeleteCharacters() throws Exception{
		super(Tables.characters);
	}
	
	private class DeleteWorker extends GenericWorker {

		public DeleteWorker(Tables table, int recordId, EditAndDeleteGenericCardPanel card){
			super("delete", table, recordId, card);
		}

		protected ExecutionResult doInBackground() throws Exception {
			return  databaseManager.executeDelete(table, recordId);
		}
	}

}
