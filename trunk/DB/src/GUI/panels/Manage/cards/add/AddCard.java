package GUI.panels.Manage.cards.add;

import Enums.Tables;
import GUI.panels.Manage.cards.GenericCardPanel;

public abstract class AddCard extends GenericCardPanel{
	private static final long serialVersionUID = 1L;

	public AddCard(Tables table, boolean isSimpleCard){
		super(table, isSimpleCard);
		if (!AddCharacters.class.isInstance(this)){
			switchCard(MAIN_CARD);
		}
	}
	
	public AddCard(Tables table){
		this(table, true);
	}
	
	public String getCardAction(){
		return "add";
	}
	
}
