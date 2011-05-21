package GUI.workers;

import GUI.commons.Pair;
import GUI.panels.Manage.cards.edit.EditCharacters;

public class EditCharacterWorker extends GenericWorker {

	protected Pair[][] values;
	protected String [] tables ;
	
	
	public EditCharacterWorker(String[] tables, Pair[][] values, EditCharacters card) {
		super(Action.EDIT, card);
		this.tables = tables;
		this.values = values;

	}

	@Override
	protected ResultHolder doInBackground() {
		throw new UnsupportedOperationException();

	}

}
