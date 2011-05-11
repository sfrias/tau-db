package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.model.CharacterModel;
import GUI.panels.Manage.cards.add.AddCharacters;

public class GetCharacterRecordsWorker extends GenericWorker{
	
	public GetCharacterRecordsWorker(AddCharacters parent) {
		super(parent);
	}
	
	
	@Override
	protected ResultHolder doInBackground(){	
		//TODO add exceptions
		CharacterModel model = new CharacterModel();
		model.setCreators(databaseManager.executeQueryAndGetValues(Tables.creator, 3));
		model.setDiseases(databaseManager.executeQueryAndGetValues(Tables.disease, 3));
		model.setEthnicities(databaseManager.executeQueryAndGetValues(Tables.ethnicity, 3));
		model.setGenders(databaseManager.executeQueryAndGetValues(Tables.gender, 3));
		model.setJobs(databaseManager.executeQueryAndGetValues(Tables.job, 3));
		model.setLocations(databaseManager.executeQueryAndGetValues(Tables.location, 3));
		model.setOccupations(databaseManager.executeQueryAndGetValues(Tables.occupation, 3));
		model.setorganizations(databaseManager.executeQueryAndGetValues(Tables.organization, 3));
		model.setPowers(databaseManager.executeQueryAndGetValues(Tables.power, 3));
		model.setRanks(databaseManager.executeQueryAndGetValues(Tables.rank, 3));
		model.setSchools(databaseManager.executeQueryAndGetValues(Tables.school, 3));
		model.setSpecies(databaseManager.executeQueryAndGetValues(Tables.species, 3));
		model.setUniverses(databaseManager.executeQueryAndGetValues(Tables.universe, 3));
		
		ResultHolder result = new ResultHolder(model, ExecutionResult.Success_Characters_Query);
		return result;
	}

}
