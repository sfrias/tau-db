package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GUI.GuiHandler;
import GUI.commons.Pair;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import GUI.workers.EditWorker;
import Utils.Tables;

public class EditCard extends EditAndDeleteGenericCardPanel{
	private static final long serialVersionUID = -1543391049010263975L;
	
	public EditCard(Tables table, boolean isSimpleCard){
		super(table ,isSimpleCard);
	}
	
	public EditCard(Tables table){
		this(table ,true);
	}

	public String getCardAction(){
		return "edit";
	}

	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				Pair selectedPair = (Pair) cb.getSelectedItem();
				int recordId = selectedPair.getId();
				EditWorker worker = new EditWorker(table, textName.getText(), recordId,thisCard);
				GuiHandler.startStatusFlash();
				worker.execute();
				//TODO - update combo
			}
		};
	}

}
