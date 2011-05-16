package GUI.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import Enums.ExecutionResult;
import GUI.GuiHandler;
import GUI.frames.PlayFrame;
import GUI.model.CharacterModel;
import GUI.model.SimpleModel;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.panels.Manage.cards.add.AddSimpleCard;
import db.DatabaseManager;

public abstract class GenericWorker extends SwingWorker<ResultHolder, Void>{
	
	protected DatabaseManager databaseManager = DatabaseManager.getInstance();
	private String action;
	
	private EditAndDeleteGenericCardPanel simpleEditDeleteCard;
	private AddSimpleCard simpleAddCard;
	private AddCharacters addCharCard;
	private PlayFrame playFrame;


	public GenericWorker(PlayFrame playFrame){
		super();

		this.playFrame = playFrame;
	}

	public GenericWorker(String action, EditAndDeleteGenericCardPanel card) {
		super();

		this.action = action;
		this.simpleEditDeleteCard = card;
	}

	public GenericWorker(String action, AddSimpleCard card){
		this.action = action;
		this.simpleAddCard = card;
	}

	public GenericWorker(String action, AddCharacters card) {
		super();

		this.action = action;
		this.addCharCard = card;
	}

	//get list for general edit/delete
	public GenericWorker(EditAndDeleteGenericCardPanel card){
		this(null, card);
	}

	//get all lists for combos in addCharacter
	public GenericWorker(AddCharacters addCharCard){
		this(null, addCharCard);
	}


	@Override
	public void done(){
		try {
			CharacterModel charModel;
			SimpleModel simpleModel;

			GuiHandler.stopStatusFlash();
			ResultHolder result = get();
			ExecutionResult e = result.getExecutionResult();
			switch (e){
			case Success_Simple_Add_Edit_Delete:  //
				GuiHandler.showResultSuccessDialog(action);
				if (action.equals("add")){
					simpleAddCard.refreshCards();
				}else{
					simpleEditDeleteCard.refreshCards();
				}
				break;
			case Success_Add_Character:    //for even sub fields
				GuiHandler.showResultSuccessDialog(action);
				charModel = (CharacterModel)result.getModel();
				addCharCard.setModel(charModel);
				addCharCard.refreshFromModel();
				break;
			case Success_Simple_Query:
				simpleModel = (SimpleModel)result.getModel();
				if (simpleEditDeleteCard != null){
					simpleEditDeleteCard.setModel(simpleModel);
					simpleEditDeleteCard.refreshFromModel();
				} else{
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
				GuiHandler.ShowResultIntegrityDialog();
				break;
			case Exception:
				GuiHandler.ShowResultExceptionDialog(action);
				break;
			case Success_Add_Character_Attribute:
				charModel = (CharacterModel)result.getModel();
				addCharCard.setModel(charModel);
				addCharCard.refreshFromModel();
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
