package GUI.workers;

import GUI.frames.PlayFrame;
import GUI.model.AlgorithmModel;
import core.Algorithm;
import core.AlgorithmUtilities;
import core.ReturnElement;
import dataTypes.ResultHolder;
import enums.ConnectionResult;
import enums.ExecutionResult;

public class AlgorithmWorker extends GenericWorker{

	private int firstCharId;
	private int secondCharId;

	public AlgorithmWorker(int firstCharId, int secondCharId, PlayFrame playFrame) {
		super(Action.ALGROTITHM, playFrame);
		this.firstCharId = firstCharId;
		this.secondCharId = secondCharId;
	}

	@Override
	protected ResultHolder doInBackground() {

		Algorithm alg = Algorithm.getInstance();
		alg.initialization();
		ReturnElement returnElem = alg.lookForConnection(firstCharId, secondCharId);
		String firstCharName = alg.getStartName();
		String secondCharName = alg.getEndName();
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
