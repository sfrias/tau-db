package GUI.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import Enums.ExecutionResult;
import Enums.Frames;
import Enums.Tables;
import GUI.GuiHandler;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import db.DatabaseManager;

public abstract class GenericWorker extends SwingWorker<ExecutionResult, Void> {
	protected String[] fieldNames;
	protected String[] values;
	protected Tables table;
	protected int recordId;
	protected EditAndDeleteGenericCardPanel card;
	
	private final String ACTION;
	
	protected static DatabaseManager databaseManager = DatabaseManager.getInstance();
	
	public GenericWorker(String action, Tables table, int recordId, EditAndDeleteGenericCardPanel card){
		this(action, table, null, null, recordId, card);
	}
	
	public GenericWorker(String action, Tables table, String [] fieldNames, String [] fieldValues, int recordId,  EditAndDeleteGenericCardPanel card){
		super();
		this.fieldNames = fieldNames;
		this.values = fieldValues;
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
				try {
					card.refreshCards();
				} catch (Exception e) {
					GuiHandler.ShowErrorGetRecords();
					GuiHandler.switchFrames(Frames.WelcomeScreenFrame);
				}
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
