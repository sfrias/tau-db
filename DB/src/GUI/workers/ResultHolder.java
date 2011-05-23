package GUI.workers;

import enums.ExecutionResult;

public class ResultHolder {
	private Object model ;
	private ExecutionResult result;
	
	public ResultHolder(Object model, ExecutionResult result){
		this.model = model;
		this.result = result;
	}
	
	public ResultHolder(ExecutionResult result){
		this.model = null;
		this.result = result;
	}
	
	public void setModel(Object model){
		this.model = model;
	}
	
	public Object getModel(){
		return model;
	}
	
	public void setExecutionResult(ExecutionResult result){
		this.result = result;
	}
	
	public ExecutionResult getExecutionResult(){
		return result;
	}

}
