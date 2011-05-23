package GUI.workers;

import GUI.commons.Pair;
import GUI.panels.Manage.cards.edit.EditCharacters;

public class EditCharacterWorker extends GenericWorker {

	private Pair[][] addedValues;
	private Pair[][] removedValues;
	private String [] tables ;
	
	
	public EditCharacterWorker(String[] tables, Pair[][] addedValues, Pair[][] removedValues, EditCharacters card) {
		super(Action.EDIT, card);
		this.tables = tables;
		this.addedValues = addedValues;
		this.removedValues = removedValues;
	}

	@Override
	protected ResultHolder doInBackground() {
		throw new UnsupportedOperationException();
	}

}
