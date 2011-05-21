package GUI.panels.Manage.cards.edit;

import Enums.Tables;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;

public abstract class EditCard extends EditAndDeleteGenericCardPanel{
	private static final long serialVersionUID = -1543391049010263975L;
	
	public EditCard(Tables table, boolean isSimpleCard){
		super(table ,isSimpleCard);
	}

	public EditCard(Tables table, boolean isSimpleCard, boolean generateRecords) {
		super(table, isSimpleCard, generateRecords);
	}

	public String getCardAction(){
		return "edit";
		
	}

}
