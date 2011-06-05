package GUI.workers;

import java.sql.SQLException;

import GUI.GuiHandler;
import GUI.frames.PlayFrame;
import GUI.model.AlgorithmModel;
import core.Algorithm;
import core.AlgorithmUtilities;
import dataTypes.Character;
import dataTypes.ResultHolder;
import dataTypes.ReturnElement;
import enums.ConnectionResult;
import enums.ExecutionResult;

public class AlgorithmWorker extends GenericWorker{


	public AlgorithmWorker(Character firstChar, Character secondChar, PlayFrame playFrame) {
		super(Action.ALGROTITHM, firstChar, secondChar, playFrame);
	}

	@Override
	protected ResultHolder doInBackground() {
		GuiHandler.startCountDown(this);
		
		Algorithm alg;
		try {
			alg = Algorithm.getInstance();
		} catch (SQLException e) { //cannot get a new instance of algorithm during error in getting the instance of databaseManager
			e.printStackTrace();
			return new ResultHolder(ExecutionResult.Exception);
		}
		alg.initialization();
		
		ReturnElement returnElem = alg.lookForConnection(firstChar, secondChar);
		String firstCharName = firstChar.getCharName();
		String secondCharName = secondChar.getCharName();
		if (returnElem.getResult() == ConnectionResult.Exception || returnElem.getResult() == ConnectionResult.Close_Exception){
			return new ResultHolder(ExecutionResult.Exception);
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
