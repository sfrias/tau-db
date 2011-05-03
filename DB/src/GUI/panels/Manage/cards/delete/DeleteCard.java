package GUI.panels.Manage.cards.delete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import GUI.commons.Pair;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import Utils.ExecutionResult;
import Utils.Tables;

public class DeleteCard extends EditAndDeleteGenericCardPanel{

	private static final long serialVersionUID = 8694645404553404464L;

	DeleteCard(Tables table, boolean isSimpleCard){
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
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Pair selectedPair = (Pair) cb.getSelectedItem();
						ExecutionResult result = databaseManager.executeDelete(table, selectedPair.getId());
						//TODO - update combo
					}
				});
			}
		};
	}
}
