package GUI.panels.Manage.cards.delete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.model.SimpleModel;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import GUI.workers.DeleteWorker;
import dataTypes.Pair;
import enums.Tables;

public class DeleteCard extends EditAndDeleteGenericCardPanel{

	private static final long serialVersionUID = 1L;
	private SimpleModel model;
	protected DeleteCard thisCard = this;


	public DeleteCard(Tables table, boolean isSimpleCard) {
		super(table, isSimpleCard);
		textName.setEditable(false);
	}
	
	public DeleteCard(Tables table){
		this(table, true);
	}

	public DeleteCard(Tables table, boolean isSimpleTable, boolean generateRecords) {
		super(table, isSimpleTable, generateRecords);
		textName.setEditable(false);
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
				try{
					Pair selectedPair = (Pair) cb.getSelectedItem();
					int recordId = selectedPair.getId();
					DeleteWorker worker = new DeleteWorker(table, recordId, thisCard);
					GuiHandler.startStatusFlash();
					worker.execute();
					
					cb.removeAllItems();
					textName.setText(null);
				}catch (ClassCastException e){
					GuiHandler.showChooseFromComboDialog();
				}
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
