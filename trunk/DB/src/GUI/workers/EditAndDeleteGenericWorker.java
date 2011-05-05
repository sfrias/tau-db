package GUI.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import db.DatabaseManager;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.GuiHandler;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;

public abstract class EditAndDeleteGenericWorker extends SwingWorker<ExecutionResult, Void> {
	protected String[] fieldNames;
	protected String[] values;
	protected Tables table;
	protected int recordId;
	protected EditAndDeleteGenericCardPanel card;
	
	private final String ACTION;
	
	protected static DatabaseManager databaseManager = DatabaseManager.getInstance();
	
	public EditAndDeleteGenericWorker(String action, Tables table, int recordId, EditAndDeleteGenericCardPanel card){
		this(action, table, null, recordId, card);
	}
	
	public EditAndDeleteGenericWorker(String action, Tables table, String fieldValue, int recordId,  EditAndDeleteGenericCardPanel card){
		super();
		fieldNames = new String[]{table.toString() + "_name"};
		values = fieldValue == null ? null : new String[] {fieldValue};
		this.table = table;
		this.recordId = recordId;
		this.ACTION = action;
		this.card = card;
	}
	
	@Override
	public void done(){
		GuiHandler.stopStatusFlash();
		try {
			ExecutionResult result = get();
			switch (result){
				case Success:
					GuiHandler.showResultSuccessDialog(ACTION);
					card.refreshCards();
					break;
				case IntegrityConstraintViolationException:
					GuiHandler.ShowResultIntegrityDialog();
					break;
				case Exception:
					GuiHandler.ShowResultExceptionDialog(ACTION);
					break;
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
