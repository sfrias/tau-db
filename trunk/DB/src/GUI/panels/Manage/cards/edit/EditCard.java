package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import GUI.commons.Pair;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import Utils.ExecutionResult;
import Utils.Tables;

public class EditCard extends EditAndDeleteGenericCardPanel{

	private static final long serialVersionUID = -1543391049010263975L;

	public EditCard(Tables table, boolean isSimpleCard){
		super(table ,isSimpleCard);
	}
	
	public EditCard(Tables table){
		super(table ,true);
	}

	public String getCardAction(){
		return "edit";
	}

	@Override
	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						String [] fieldNames = {table.toString() + "_name"};
						String [] values = {textName.getText()};
						Pair selectedPair = (Pair) cb.getSelectedItem();
						ExecutionResult result = databaseManager.executeUpdate(table, fieldNames, values, selectedPair.getId());
					}
				});
			}
		};
	}

}
