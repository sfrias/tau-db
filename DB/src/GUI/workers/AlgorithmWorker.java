package GUI.workers;

import GUI.GuiHandler;
import GUI.model.AlgorithmModel;
import GUI.panels.Play.Tabs.MainPlayTab;
import core.Algorithm;
import core.AlgorithmUtilities;
import dataTypes.Character;
import dataTypes.ResultHolder;
import dataTypes.ReturnElement;
import enums.ConnectionResult;
import enums.ExecutionResult;

public class AlgorithmWorker extends GenericWorker{


	public AlgorithmWorker(Character firstChar, Character secondChar, MainPlayTab playTab) {
		super(Action.ALGROTITHM, firstChar, secondChar, playTab);
	}

	@Override
	protected ResultHolder doInBackground() {
	//	GuiHandler.startCountDown(this);
		
		Algorithm alg = Algorithm.getInstance();
		
		alg.initialization();
		
		ReturnElement returnElem = alg.lookForConnection(firstChar, secondChar);
		String firstCharName = firstChar.getCharName();
		String secondCharName = secondChar.getCharName();
		if (returnElem.getResult() == ConnectionResult.Exception || returnElem.getResult() == ConnectionResult.Close_Exception){
			return new ResultHolder(ExecutionResult.Exception);
		}
		else if (returnElem.getResult() == ConnectionResult.Character_not_found){
			AlgorithmModel model = new AlgorithmModel(ConnectionResult.Character_not_found, "One of the characters must have been deleted from the database." +
					"\n Please refresh your search.");
			return new ResultHolder(model, ExecutionResult.Success_Algorithm);
		}
		else if (returnElem.getResult() == ConnectionResult.Did_Not_Find_Connection){
			AlgorithmModel model = new AlgorithmModel(ConnectionResult.Did_Not_Find_Connection, "Could not find a connection between " + firstCharName + " and " + secondCharName);
			return new ResultHolder(model, ExecutionResult.Success_Algorithm);
		}
		else if (returnElem.getResult() == ConnectionResult.Found_Connection_Of_Length_0){
			AlgorithmModel model = new AlgorithmModel(ConnectionResult.Found_Connection_Of_Length_0, "Source character and destination character need to be different");
			return new ResultHolder(model, ExecutionResult.Success_Algorithm);		
		}

		else{
			String[] connectionChain = AlgorithmUtilities.readConnectionChain(returnElem.getConnectionArray());
			StringBuilder stringBuilder = new StringBuilder();
			for (int i=0; i< connectionChain.length; i++){
				if (connectionChain[i]!=null){
					stringBuilder.append(connectionChain[i].toString() + "\n");
				}
			}

			AlgorithmModel model = new AlgorithmModel(ConnectionResult.Found_Connection, stringBuilder.toString());
			return new ResultHolder(model, ExecutionResult.Success_Algorithm);		
		}
	}
}
