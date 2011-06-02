package GUI.workers;

import GUI.panels.Manage.cards.edit.EditCharacters;
import dataTypes.Pair;
import dataTypes.ResultHolder;
import enums.ExecutionResult;
import enums.Tables;

public class EditCharacterWorker extends GenericWorker {

	private Pair[][] addedValues;
	private Pair[][] removedValues;
	private Tables [] tables ;
	private Pair character;
	private int placeOfBirthId;
	
	
	public EditCharacterWorker(Tables[] tables, Pair[][] addedValues, Pair[][] removedValues, Pair character, int placeOfBirthId, EditCharacters card) {
		super(Action.EDIT, card);
		this.tables = tables;
		this.addedValues = addedValues;
		this.removedValues = removedValues;
		this.character = character;
		this.placeOfBirthId = placeOfBirthId;
	}

	@Override
	protected ResultHolder doInBackground() {
		
		ExecutionResult result = databaseManager.executeEditCharacters(tables, addedValues, removedValues, character, placeOfBirthId);
		ResultHolder resultHolder = new ResultHolder(result);
		return resultHolder;
	}

}
