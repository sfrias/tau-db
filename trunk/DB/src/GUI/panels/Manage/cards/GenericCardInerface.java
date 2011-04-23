package GUI.panels.Manage.cards;

import java.awt.event.ActionListener;

public interface GenericCardInerface {
	
	//each card should add diffrent fields. 
	//card implement
	public void addFields();
	
	//each card should have diffrent action. 
	//abstract implement
	public String getCardAction();
	
	//each card should have a diffrent action - depends on the fields. 
	///card implement
	public ActionListener createActionButtonListener();
	
	//each card should fill the fields according to the record diffrently (diffrent fields)
	//card implement
	public ActionListener createRecordComboListener();

	//each card should display a list of all its records. card implement.
	public String[] createRecordList();
}
