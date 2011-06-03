package GUI.panels.Manage.cards.add;

import GUI.panels.Manage.cards.GenericCardPanel;
import enums.Tables;

public abstract class AddCard extends GenericCardPanel{
	private static final long serialVersionUID = 1L;

	public AddCard(Tables table, boolean isSimpleCard){
		super(table, isSimpleCard);
	}
	
	public AddCard(Tables table){
		this(table, true);
	}
	
	public String getCardAction(){
		return "add";
	}
	
	public void refreshFromModel(){}
	
	public void generateRecords(){}
	
}
