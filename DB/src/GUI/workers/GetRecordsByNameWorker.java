package GUI.workers;

import enums.ExecutionResult;
import GUI.commons.Pair;
import GUI.frames.PlayFrame;
import GUI.model.SimpleModel;
import GUI.panels.Manage.cards.delete.DeleteCharacters;
import GUI.panels.Manage.cards.edit.EditCharacters;

public class GetRecordsByNameWorker extends GenericWorker {

	private String queryString;
	private int charNum;



	public GetRecordsByNameWorker(PlayFrame playFrame, int charNum, String queryString){
		super(Action.QUERY, playFrame);
		this.queryString = queryString;
		this.charNum = charNum;
	}

	public GetRecordsByNameWorker(EditCharacters card, int charNum, String queryString) {
		super(Action.QUERY, card);
		this.queryString = queryString;
		this.charNum = charNum;
	}

	public GetRecordsByNameWorker(DeleteCharacters card, int charNum, String queryString) {
		super(Action.QUERY, card);
		this.queryString = queryString;
		this.charNum = charNum;	
	}

	protected ResultHolder doInBackground(){

		Pair[] pairs =  databaseManager.getCharacters(queryString);
		
		if (pairs == null){
			return new ResultHolder(ExecutionResult.Exception);
		}
		
		SimpleModel model = new SimpleModel(pairs);		
		ExecutionResult eResult;
		if (charNum == 1){
			eResult = ExecutionResult.Success_Get_Characters_For_First_Character;
		}
		else if (charNum == 2){
			eResult = ExecutionResult.Success_Get_Characters_For_Second_Character;
		}
		else{
			eResult = ExecutionResult.Success_Get_Characters;
		}
		ResultHolder result = new ResultHolder(model,eResult);
		return result;
	}
}
