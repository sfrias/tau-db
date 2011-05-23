package GUI.panels.Manage.cards.delete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import enums.Tables;


import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import GUI.model.SimpleModel;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import GUI.workers.DeleteWorker;

public class DeleteCard extends EditAndDeleteGenericCardPanel{

	private static final long serialVersionUID = 1L;
	private SimpleModel model;
	protected DeleteCard thisCard = this;


	DeleteCard(Tables table, boolean isSimpleCard) throws Exception{
		super(table, isSimpleCard);
		textName.setEditable(false);
	}
	
	DeleteCard(Tables table){
		super(table, true);
		textName.setEditable(false);
	}

	public DeleteCard(Tables table, boolean isSimpleTable, boolean generateRecords) {
		super(table, isSimpleTable, generateRecords);
	}

	public String getCardAction(){
		return "delete";
	}
	
	public void refreshFromModel(){
		comboRecord.removeAllItems();
		ComboBoxModel comboModel = new DefaultComboBoxModel(model.getRecords());
		comboRecord.setModel(comboModel);
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
				DefaultComboBoxModel model = (DefaultComboBoxModel) cb.getModel();
				Object record =  model.getSelectedItem();
				if (record != null && record instanceof Pair){
					textName.setText(((Pair)record).getName());
				}
			}
		};
	}
}
