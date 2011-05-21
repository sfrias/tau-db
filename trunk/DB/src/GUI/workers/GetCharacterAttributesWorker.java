package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.commons.Pair;
import GUI.frames.PlayFrame;
import GUI.model.CharacterModel;
import GUI.panels.Manage.cards.edit.EditCharacters;

public class GetCharacterAttributesWorker extends GenericWorker {

	private int recordId;
	private int charNum;
	
	public GetCharacterAttributesWorker(PlayFrame playFrame, int recordId, int charNum) {
		super(Action.QUERY, playFrame);
		this.recordId = recordId;
		this.charNum = charNum;
	}
	
	public GetCharacterAttributesWorker(EditCharacters card, int recordId, int charNum) {
		super(Action.QUERY, card);
		this.recordId = recordId;
		this.charNum = charNum;
	}

	@Override
	protected ResultHolder doInBackground(){
		//TODO add exceptions		
		String [] tables = getTablesNames();
		Pair[][] values = databaseManager.getCharacterAttributes(recordId, tables);
		ResultHolder result;
		
		if (values == null){
			result = new ResultHolder(null, ExecutionResult.Exception);
		}
		else{
			
			CharacterModel model = convertPairsToModel(values);
			if (model == null){
				result = new ResultHolder(null, ExecutionResult.Exception);
			}
			else{
				if (charNum == 1){
					result = new ResultHolder(model, ExecutionResult.Success_Get_Character_Attributes_For_First_Character);
				}
				else if (charNum == 2){
					result = new ResultHolder(model, ExecutionResult.Success_Get_Character_Attributes_For_Second_Character);
				}
				else{
					result = new ResultHolder(model, ExecutionResult.Success_Get_Character_Attributes);
				}
			}
			
		}
		return result;
	}

	private String[] getTablesNames() {

		String [] values = new String[Tables.getMaxIndex()+1];
		values[Tables.disease.getIndex()] = Tables.disease.name();
		values[Tables.occupation.getIndex()] = Tables.occupation.name();
		values[Tables.organization.getIndex()] = Tables.organization.name();
		values[Tables.power.getIndex()] = Tables.power.name();
		values[Tables.school.getIndex()] = Tables.school.name();
		values[Tables.universe.getIndex()] = Tables.universe.name();

		return values;
	}
	
	private CharacterModel convertPairsToModel(Pair[][] pairs) {
		
		if (pairs.length != Tables.getMaxIndex() + 1){
			return null;
		}
		else{
			
			CharacterModel model = new CharacterModel();
			model.setAttributePairs(pairs[Tables.disease.getIndex()], Tables.disease);
			model.setAttributePairs(pairs[Tables.occupation.getIndex()], Tables.occupation);
			model.setAttributePairs(pairs[Tables.organization.getIndex()], Tables.organization);
			model.setAttributePairs(pairs[Tables.power.getIndex()], Tables.power);
			model.setAttributePairs(pairs[Tables.school.getIndex()], Tables.school);
			model.setAttributePairs(pairs[Tables.universe.getIndex()], Tables.universe);
			
			return model;
			
		}
	}
}
