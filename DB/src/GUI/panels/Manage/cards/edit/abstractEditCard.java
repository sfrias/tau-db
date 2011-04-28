package GUI.panels.Manage.cards.edit;

import GUI.panels.Manage.cards.GenericCardPanel;

public abstract class abstractEditCard extends GenericCardPanel{
	
	protected int fieldsNum;
	
	public String getCardAction(){
		return "edit";
	}

}
