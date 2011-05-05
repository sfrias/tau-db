package GUI.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.GuiHandler;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import db.DatabaseManager;

public class AddWorker extends SwingWorker<ExecutionResult, Void>{
	
	protected String[] fieldNames;
	protected String[] values;
	protected Tables table;
	protected int recordId;
	protected EditAndDeleteGenericCardPanel card;
	
	private final String ACTION;
	
	protected static DatabaseManager databaseManager = DatabaseManager.getInstance();
	
	public AddWorker(Tables table, String [] fieldNames, String [] fieldValues){
		super();
		this.fieldNames = fieldNames;
		this.values = fieldValues;
		this.table = table;
		this.ACTION = "add";
	}
	
	@Override
	public void done(){
		GuiHandler.stopStatusFlash();
		try {
			ExecutionResult result = get();
			switch (result){
				case Success:
					GuiHandler.showResultSuccessDialog(ACTION);
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

	@Override
	protected ExecutionResult doInBackground() throws Exception {
		return databaseManager.executeInset(table, fieldNames, values);		
	}

}
