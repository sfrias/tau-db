package GUI.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import Enums.ExecutionResult;
import GUI.GuiHandler;
import GUI.frames.PlayFrame;
import GUI.model.CharacterModel;
import GUI.model.SimpleModel;
import GUI.panels.Manage.cards.GenericCardPanel;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.panels.Manage.cards.add.AddSimpleCard;
import GUI.panels.Manage.cards.delete.DeleteCard;
import GUI.panels.Manage.cards.delete.DeleteCharacters;
import GUI.panels.Manage.cards.edit.EditCharacters;
import GUI.panels.Manage.cards.edit.EditSimpleCard;
import db.DatabaseManager;

public abstract class GenericWorker extends SwingWorker<ResultHolder, Void>{

	enum Action{ADD, EDIT, DELETE, QUERY};

	protected DatabaseManager databaseManager = DatabaseManager.getInstance();
	private Action action;

	private GenericCardPanel card;
	private PlayFrame playFrame;

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

	/*	//get list for general edit/delete
	public GenericWorker(EditAndDeleteGenericCardPanel card){
		this(null, card);
	}

	//get all lists for combos in addCharacter
	public GenericWorker(AddCharacters addCharCard){
		this(null, addCharCard);
	}*/


	@Override
	public void done(){
		try {
			CharacterModel charModel;
			GuiHandler.stopStatusFlash();
			ResultHolder result = get();
			ExecutionResult e = result.getExecutionResult();
			switch (e){
			case Success_Simple_Add_Edit_Delete:{
				GuiHandler.showResultSuccessDialog(action.toString());
				card.refreshFromModel();
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
					editCharCard.refreshFromCharacterModel();					
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
					editCharCard1.refreshFromCharacterModel();					
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
