package GUI.panels.Manage.cards;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComponent;

public interface GenericCardInerface {
	
	//each card should add diffrent fields. 
	//card implement
	public void addFields(Vector<String> fieldsNames, Vector<JComponent> fieldsComponents, Vector<JComponent> extraFields);
	
	//each card should have diffrent action. 
	//abstract implement
	public String getCardAction();
	
	//each card should have a diffrent action - depends on the fields. 
	///card implement
	public ActionListener createActionButtonListener();
}
