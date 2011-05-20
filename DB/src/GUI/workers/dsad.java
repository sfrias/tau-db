package GUI.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import Enums.ExecutionResult;
import GUI.GuiHandler;
import GUI.frames.PlayFrame;
import GUI.model.CharacterModel;
import GUI.model.ExtendedCharacterModel;
import GUI.model.SimpleModel;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.panels.Manage.cards.add.AddSimpleCard;
import GUI.panels.Manage.cards.delete.DeleteCard;
import GUI.panels.Manage.cards.edit.EditCharacters;
import GUI.panels.Manage.cards.edit.EditSimpleCard;
import db.DatabaseManager;

public abstract class GenericWorker extends SwingWorker<ResultHolder, Void>{

	private enum Action{ADD, EDIT, DELETE};
	
	protected DatabaseManager databaseManager = DatabaseManager.getInstance();
	private Action action;

	private GenericCardPanel card;
	private AddSimpleCard simpleAddCard;
	private AddCharacters addCharCard;
	private EditSimpleCard editSimpleCard;
	private EditCharacters editCharactersCard;
	private DeleteCard deleteCard;
	private PlayFrame playFrame;

	public GenericWorker(PlayFrame playFrame){
		super();
		this.playFrame = playFrame;
	}
	
	public GenericWorker(EditSimpleCard card) {
		super();
		this.action = Action.EDIT;
		this.editSimpleCard = card;
	}
	
	public GenericWorker(EditCharacters card) {
		super();
		this.action = Action.EDIT;
		this.editCharactersCard = card;
	}
	
	public GenericWorker(DeleteCard card) {
		super();
		this.action = Action.DELETE;
		this.deleteCard = card;
	}

	public GenericWorker(AddSimpleCard card){
		this.action = Action.ADD;
		this.simpleAddCard = card;
	}

	public GenericWorker(AddCharacters card) {
		super();
		this.action = Action.ADD;
		this.addCharCard = card;
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
			case Success_Simple_Add_Edit_Delete:
				GuiHandler.showResultSuccessDialog(action.toString());
				switch (action){
				case ADD:
					simpleAddCard.refreshCards();
					break;
				case DELETE:
					deleteCard.refreshCards();
					break;
				case EDIT:
					editSimpleCard.refreshCards();
					break;				
				}
			case Success_Add_Character:    //for even sub fields
				GuiHandler.showResultSuccessDialog(action.toString());
				addCharCard.clearValues();
				break;
			case Success_Simple_Query:
				if (editDeleteCard != null){
					if (editDeleteCard instanceof EditSimpleCard){
						SimpleModel simpleModel = (SimpleModel)result.getModel();
						EditSimpleCard editSimpleCard = (EditSimpleCard) editDeleteCard;
						editSimpleCard.setModel(simpleModel);
						editSimpleCard.refreshFromModel();
					}
					else if (editDeleteCard instanceof EditCharacters){
						ExtendedCharacterModel extendedCharacterModel = (ExtendedCharacterModel)result.getModel();
						EditCharacters editCharacters = (EditCharacters) editDeleteCard;
						editCharacters.setModel(extendedCharacterModel);
						editCharacters.refreshFromModel();
					}
					else{
						SimpleModel simpleModel = (SimpleModel)result.getModel();
						DeleteCard deleteCard = (DeleteCard) editDeleteCard;
						deleteCard.setModel(simpleModel);
						deleteCard.refreshFromModel();
					}
				} else{
					SimpleModel simpleModel = (SimpleModel)result.getModel();
					playFrame.setSimpleModel(simpleModel);
					playFrame.refreshFromModel();
				}
				break;
			case Success_Characters_Query:
				charModel = (CharacterModel)result.getModel();
				addCharCard.setModel(charModel);
				addCharCard.refreshFromModel();
				break;
			case IntegrityConstraintViolationException:
				GuiHandler.showResultIntegrityDialog();
				break;
			case Exception:
				GuiHandler.showResultExceptionDialog(action.toString());
				break;
			case Success_Add_Character_Attribute:
				charModel = (CharacterModel)result.getModel();
				if (addCharCard != null){
					addCharCard.setModel(charModel);
					addCharCard.refreshFromModel();
				}
				else if (){
					editDeleteCard
					
				}

				GuiHandler.showResultSuccessDialog(action);
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
