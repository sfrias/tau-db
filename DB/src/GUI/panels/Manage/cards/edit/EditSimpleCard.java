package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import GUI.workers.EditWorker;

public class EditSimpleCard extends EditCard{
	
	private static final long serialVersionUID = 1L;

	public EditSimpleCard(Tables table) throws Exception {
		super(table, true);
	}

	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				Pair selectedPair = (Pair) cb.getSelectedItem();
				int recordId = selectedPair.getId();
				String [] fieldNames = new String[]{table.toString()+"_name"};
				String [] fieldValues = new String[]{textName.getText()};
				EditWorker worker = new EditWorker(table, fieldNames, fieldValues, recordId,thisCard);
				GuiHandler.startStatusFlash();
				worker.execute();
				//TODO - update combo
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

}
