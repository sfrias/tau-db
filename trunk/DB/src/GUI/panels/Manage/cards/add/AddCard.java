package GUI.panels.Manage.cards.add;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.panels.Manage.Tabs.AddWorker;
import GUI.panels.Manage.cards.GenericCardPanel;

public abstract class AddCard extends GenericCardPanel{
	private static final long serialVersionUID = 1L;

	public AddCard(Tables table, boolean isSimpleCard){
		super(table, isSimpleCard);
	}
	
	public AddCard(Tables table){
		super(table, true);
	}
	
	public String getCardAction(){
		return "add";
	}
	
	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						String [] fieldNames = {table.toString()+"_name"};
						String [] values = {textName.getText()};
						AddWorker worker = new AddWorker(table, fieldNames, values);
						GuiHandler.startStatusFlash();
						worker.execute();
					}
				});
			}
		};
	}
}
