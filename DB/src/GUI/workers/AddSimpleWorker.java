package GUI.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.GuiHandler;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import db.DatabaseManager;

public class AddSimpleWorker extends SwingWorker<ExecutionResult, Void>{
	
	protected String fieldName;
	protected String value;
	protected Tables table;
	protected int recordId;
	protected EditAndDeleteGenericCardPanel card;
	
	private final String ACTION;
	
	protected static DatabaseManager databaseManager = DatabaseManager.getInstance();
	
	public AddSimpleWorker(Tables table, String fieldName, String fieldValue){
		super();
		this.fieldName = fieldName;
		this.value = fieldValue;
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
		return databaseManager.executeSimpleInsert(table, fieldName, value);		
	}

}
