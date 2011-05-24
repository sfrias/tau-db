package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import GUI.model.SimpleModel;
import GUI.workers.EditSimpleWorker;
import enums.Tables;

public class EditSimpleCard extends EditCard{
	
	private static final long serialVersionUID = 1L;
	private SimpleModel model;
	protected EditSimpleCard thisCard = this;


	public EditSimpleCard(Tables table){
		super(table, true);
	}
	
	public void refreshFromModel(){
		comboRecord.removeAllItems();
		Pair[] pairs = model.getRecords();
		for (int i=0; i<pairs.length; i++){
			comboRecord.addItem(pairs[i]);
		}
		comboRecord.setSelectedItem(null);
		textName.setText("");
	}
	
	public void setModel(SimpleModel model){
		this.model = model;
	}
	
	public SimpleModel getModel(){
		return model;
	}

	public ActionListener createActionButtonListener() {
		
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				Pair selectedPair = (Pair) cb.getSelectedItem();
				int recordId = selectedPair.getId();
				String [] fieldNames = new String[]{table.name()+"_name"};
				String [] fieldValues = new String[]{textName.getText()};
				EditSimpleWorker worker = new EditSimpleWorker(table, fieldNames, fieldValues, recordId,thisCard);
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
					textName.setText(record.getName());
				}
			}
		};
	}

}
