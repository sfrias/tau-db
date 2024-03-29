package GUI.panels.Manage.cards.add;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GUI.GuiHandler;
import GUI.workers.AddSimpleWorker;
import enums.Tables;

public class AddSimpleCard extends AddCard {

	private static final long serialVersionUID = 1L;
	private AddSimpleCard me = this;
	public AddSimpleCard(Tables table) {
		super(table);
	}

	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {

				String fieldName = table.name()+"_name";
				String value = textName.getText();
				AddSimpleWorker worker = new AddSimpleWorker(table, fieldName, value, me);
				GuiHandler.startStatusFlash();
				worker.execute();
			}
		};
	}

}
