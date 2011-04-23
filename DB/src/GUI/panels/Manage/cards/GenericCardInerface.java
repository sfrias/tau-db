package GUI.panels.Manage.cards;

import java.awt.event.ActionListener;

public interface GenericCardInerface {
	
	//each card should add diffrent fields. card implement
	public void addFields();
	
	//each card should have diffrent action. abstract implement
	public String getCardAction();
	
	//each card should have a diffrent action - depends on the fields. card implement
	public ActionListener setActionButtonListener();

}
