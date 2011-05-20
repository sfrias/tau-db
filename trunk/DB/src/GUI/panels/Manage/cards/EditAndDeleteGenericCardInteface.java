package GUI.panels.Manage.cards;

import java.awt.event.ActionListener;

public interface EditAndDeleteGenericCardInteface extends GenericCardInerface {
	
	//each card should fill the fields according to the record differently (different fields)
	//card implement
	public ActionListener createRecordComboListener();

	public void refreshFromModel();
	
	public void generateRecords();
	
}
