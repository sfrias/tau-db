package GUI.panels.Manage.cards.add;

import Utils.Tables;
import GUI.panels.Manage.cards.GenericCardPanel;

public abstract class abstractAddCard extends GenericCardPanel{
	private static final long serialVersionUID = 1L;

	public abstractAddCard(Tables table) {
		super(table);
	}

	public String getCardAction(){
		return "add";
	}
}
