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
		values[0] = Tables.creator.name();
		values[1] = Tables.disease.name();
		values[2] = Tables.ethnicity.name();
		values[3] = Tables.gender.name();
		values[4] = Tables.job.name();
		values[5] = Tables.occupation.name();
		values[6] = Tables.organization.name();
		values[7] = Tables.power.name();
		values[8] = Tables.rank.name();
		values[9] = Tables.school.name();
		values[10] = Tables.species.name();
		values[11] = Tables.universe.name();

		return values;
	}
	
	private CharacterModel convertPairsToModel(Pair[][] pairs) {
		
		if (pairs.length != Tables.getMaxIndex() + 1){
			return null;
		}
		else{
			
			CharacterModel model = new CharacterModel();
			model.setAttributePairs(pairs[0], Tables.creator);
			model.setAttributePairs(pairs[1], Tables.disease);
			model.setAttributePairs(pairs[2], Tables.ethnicity);
			model.setAttributePairs(pairs[3], Tables.gender);
			model.setAttributePairs(pairs[4], Tables.job);
			model.setAttributePairs(pairs[5], Tables.occupation);
			model.setAttributePairs(pairs[6], Tables.organization);
			model.setAttributePairs(pairs[7], Tables.power);
			model.setAttributePairs(pairs[8], Tables.rank);
			model.setAttributePairs(pairs[9], Tables.school);
			model.setAttributePairs(pairs[10], Tables.species);
			model.setAttributePairs(pairs[11], Tables.universe);
			
			return model;
			
		}
	}
}
