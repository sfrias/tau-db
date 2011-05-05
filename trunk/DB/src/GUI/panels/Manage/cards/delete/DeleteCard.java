package GUI.panels.Manage.cards.delete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import GUI.workers.GenericWorker;

public class DeleteCard extends EditAndDeleteGenericCardPanel{

	private static final long serialVersionUID = 8694645404553404464L;

	DeleteCard(Tables table, boolean isSimpleCard) throws Exception{
		super(table, isSimpleCard);
		textName.setEditable(false);
	}
	
	DeleteCard(Tables table) throws Exception{
		super(table, true);
		textName.setEditable(false);
	}

	public String getCardAction(){
		return "delete";
	}

	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				Pair selectedPair = (Pair) cb.getSelectedItem();
				int recordId = selectedPair.getId();
				DeleteWorker worker = new DeleteWorker(table, recordId, thisCard);
				//TODO - update combo
				GuiHandler.startStatusFlash();
				worker.execute();
			}
		};
	}
	
	public ActionListener createRecordComboListener() { 

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cb = (AutoCompleteComboBox)e.getSource();
				Pair record = (Pair) cb.getSelectedItem();
				if (record != null){
					String [] valuesArr = databaseManager.getCurrentValues(table, table.toString()+"_id", record.getId());
					textName.setText(valuesArr[0]);
				}
			}
		};
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
