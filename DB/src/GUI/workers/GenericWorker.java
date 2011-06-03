package GUI.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import GUI.GuiHandler;
import GUI.frames.PlayFrame;
import GUI.model.AlgorithmModel;
import GUI.model.CharacterModel;
import GUI.model.SimpleModel;
import GUI.model.StatisticsModel;
import GUI.panels.Manage.Tabs.StatisticsTab;
import GUI.panels.Manage.cards.GenericCardPanel;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.panels.Manage.cards.add.AddSimpleCard;
import GUI.panels.Manage.cards.delete.DeleteCard;
import GUI.panels.Manage.cards.delete.DeleteCharacters;
import GUI.panels.Manage.cards.edit.EditCharacters;
import GUI.panels.Manage.cards.edit.EditSimpleCard;
import dataTypes.ResultHolder;
import database.DatabaseManager;
import enums.ConnectionResult;
import enums.ExecutionResult;

public abstract class GenericWorker extends SwingWorker<ResultHolder, Void>{

	enum Action{ADD, EDIT, DELETE, QUERY, ALGROTITHM, STATISTICS};

	protected DatabaseManager databaseManager = DatabaseManager.getInstance();
	private Action action;

	private GenericCardPanel card;
	private PlayFrame playFrame;
	private StatisticsTab statisticsTab;

	public GenericWorker(){
		super();
	}
	
	public GenericWorker(StatisticsTab statisticTab){
		super();
		this.statisticsTab = statisticTab;
		this.action = Action.STATISTICS;
	}

	public GenericWorker(Action action, PlayFrame playFrame){
		super();
		this.action = action;
		this.playFrame = playFrame;
	}

	public GenericWorker(EditSimpleCard card) {
		super();
		this.action = Action.EDIT;
		this.card = card;
	}

	public GenericWorker(Action action, EditCharacters card) {
		super();
		this.action = action;
		this.card = card;
	}

	public GenericWorker(DeleteCard card) {
		super();
		this.action = Action.DELETE;
		this.card = card;
	}

	public GenericWorker(AddSimpleCard card){
		this.action = Action.ADD;
		this.card = card;
	}

	public GenericWorker(AddCharacters card) {
		super();
		this.action = Action.ADD;
		this.card = card;
	}


	public GenericWorker(Action action, DeleteCharacters card) {
		super();
		this.action = action;
		this.card = card;	
	}

	@Override
	public void done(){
		if (isCancelled()){
			GuiHandler.stopStatusFlash();
			GuiHandler.showResultExceptionDialog("TIMER-STOPPED-FOR-DEBUG-ONLY");
			
		}else{

			try {
				CharacterModel charModel;
				GuiHandler.stopStatusFlash();
				ResultHolder result = get();
				ExecutionResult e = result.getExecutionResult();
				switch (e){
				case Success_Simple_Add_Edit_Delete:{
					GuiHandler.showResultSuccessDialog(action.toString());
					break;
				}
				case Success_Add_Character:{
					GuiHandler.showResultSuccessDialog(action.toString());
					((AddCharacters)card).clearValues();
					break;
				}
				case Success_Simple_Query:{
					if (card != null){
						SimpleModel simpleModel = (SimpleModel)result.getModel();
						if (card instanceof EditSimpleCard){
							EditSimpleCard editSimpleCard = (EditSimpleCard) card;
							editSimpleCard.setModel(simpleModel);
							editSimpleCard.refreshFromModel();
						}
						else if (card instanceof EditCharacters){
							EditCharacters editCharacters = (EditCharacters) card;
							editCharacters.setSimpleModel(simpleModel);
							editCharacters.refreshFromModel();
						}
						else{
							DeleteCard deleteCard = (DeleteCard) card;
							deleteCard.setModel(simpleModel);
							deleteCard.refreshFromModel();
						}
					}
					break;
				}
				case Success_Characters_Query:{
					charModel = (CharacterModel)result.getModel();
					if (card instanceof AddCharacters){
						AddCharacters addCharCard = (AddCharacters) card;
						addCharCard.setModel(charModel);
						addCharCard.refreshFromModel();
					}
					else{
						EditCharacters editCharCard = (EditCharacters) card;
						editCharCard.setCharacterModel(charModel);
						editCharCard.refreshAllAttributesFromCharacterModel();					
					}
					break;
				}
				case IntegrityConstraintViolationException:
					GuiHandler.showResultIntegrityDialog();
					break;
				case Exception:
					GuiHandler.showResultExceptionDialog(action.toString());
					break;
				case Success_Add_Character_Attribute:{
					charModel = (CharacterModel)result.getModel();
					if (card instanceof AddCharacters){
						AddCharacters addCharCard1 = (AddCharacters) card;
						addCharCard1.setModel(charModel);
						addCharCard1.refreshFromModel();
					}
					else{
						EditCharacters editCharCard1 = (EditCharacters) card;
						editCharCard1.setCharacterModel(charModel);
						editCharCard1.refreshAllAttributesFromCharacterModel();					
					}
					GuiHandler.showResultSuccessDialog(action.toString());
					break;
				}
				case Success_Get_Characters:{
					SimpleModel simpleModel = (SimpleModel)result.getModel();
					if (card instanceof EditCharacters){
						EditCharacters editCharacters = (EditCharacters) card;
						editCharacters.setSimpleModel(simpleModel);
						editCharacters.refreshFromModel();
					}
					else{
						DeleteCharacters deleteCharactersCard = (DeleteCharacters) card;
						deleteCharactersCard.setModel(simpleModel);
						deleteCharactersCard.refreshFromModel();
					}
					break;
				}
				case Success_Get_Characters_For_First_Character:{
					SimpleModel simpleModel = (SimpleModel)result.getModel();
					playFrame.setSimpleModel(simpleModel);
					playFrame.refreshCharIFromModel();
					break;
				}
				case Success_Get_Characters_For_Second_Character:{
					SimpleModel simpleModel = (SimpleModel)result.getModel();
					playFrame.setSimpleModel(simpleModel);
					playFrame.refreshCharIIFromModel();
					break;
				}
				case Success_Get_Character_Attributes:{
					CharacterModel model = (CharacterModel)result.getModel();
					EditCharacters editCharactersCard = (EditCharacters) card;
					editCharactersCard.setCharacterModel(model);
					editCharactersCard.refreshCharaterAttributesFromCharacterModel();
					break;
				}
				case Success_Get_Character_Attributes_For_First_Character:{
					CharacterModel model = (CharacterModel)result.getModel();
					playFrame.refreshPanelLeftDetailsFromModel(model);
					break;
				}
				case Success_Get_Character_Attributes_For_Second_Character:{
					CharacterModel model = (CharacterModel)result.getModel();
					playFrame.refresPanelRightDetailsFromModel(model);
					break;
				}
				case Success_Edit_Character:{
					EditCharacters editCharactersCard = (EditCharacters) card;
					GuiHandler.showResultSuccessDialog(action.toString());
					editCharactersCard.clearValues();
					break;
				}
				case Success_Statistics:{
					StatisticsModel statisticModel = (StatisticsModel) result.getModel();
					statisticsTab.setModel(statisticModel);
					statisticsTab.refreshFromModel();
					break;
				}
				case Success_Algorithm:{
					AlgorithmModel model = (AlgorithmModel) result.getModel();
					ConnectionResult connResult = model.getConnResult();
					switch (connResult){

					case Did_Not_Find_Connection:
					case Found_Connection:
						GuiHandler.showAlgrithmResultDialog(true,connResult.toString(), model.getResultString());
						break;
					case Found_Connection_Of_Length_0:
						GuiHandler.showAlgrithmResultDialog(false, connResult.toString(), model.getResultString());
						break;

					default:
						break;
					}
					if (GuiHandler.isStatusFlashing()){
						GuiHandler.stopStatusFlash();
					}
				}
				default:
					break;
				}		
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
