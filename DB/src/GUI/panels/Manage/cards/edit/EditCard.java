package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import GUI.commons.Pair;
import GUI.panels.Manage.cards.GenericCardPanel;
import Utils.Tables;

public class EditCard extends GenericCardPanel{

	private static final long serialVersionUID = -1543391049010263975L;

	public EditCard(Tables table) {
		super(table);
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
						String [] values = {field1.getText()};
						Pair selectedPair = (Pair) cb.getSelectedItem();
						databaseManager.executeUpdate(table, fieldNames, values, selectedPair.getId());
					}
				});
			}
		};
	}

}
