package GUI.panels.Manage.cards.delete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import GUI.workers.DeleteWorker;

public class DeleteCard extends EditAndDeleteGenericCardPanel{

	private static final long serialVersionUID = 8694645404553404464L;

	DeleteCard(Tables table, boolean isSimpleCard) throws Exception{
		super(table, isSimpleCard);
		textName.setEditable(false);
	}
	
	DeleteCard(Tables table){
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
					//switchCard(MAIN_CARD);
					String [] valuesArr = databaseManager.getCurrentValues(table, table.toString().compareTo("characters")==0 ? "character_id" : table.toString()+"_id", record.getId());
					textName.setText(valuesArr[0]);
				}
			}
		};
	}

	
}
