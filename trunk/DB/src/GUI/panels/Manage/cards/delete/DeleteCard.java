package GUI.panels.Manage.cards.delete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import GUI.commons.Pair;
import GUI.panels.Manage.cards.GenericCardPanel;
import Utils.ExecutionResult;
import Utils.Tables;

public class DeleteCard extends GenericCardPanel{

	private static final long serialVersionUID = 8694645404553404464L;


	public DeleteCard(Tables table) {
		super(table);
	}

	public String getCardAction(){
		return "delete";
	}

	@Override
	public void addFields() {
		super.addFields();
		field1.setEditable(false);
	}


	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Pair selectedPair = (Pair) cb.getSelectedItem();
						ExecutionResult result = databaseManager.executeDelete(table, selectedPair.getId());
					}
				});
			}
		};
	}
}
