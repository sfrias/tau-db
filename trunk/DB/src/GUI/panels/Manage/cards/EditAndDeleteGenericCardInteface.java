package GUI.panels.Manage.cards;

import java.awt.event.ActionListener;

import GUI.commons.Pair;

public interface EditAndDeleteGenericCardInteface extends GenericCardInerface {
	
	//each card should fill the fields according to the record differently (different fields)
	//card implement
	public ActionListener createRecordComboListener();

	//each card should display a list of all its records. card implement.
	public Pair[] createRecordList() throws Exception;

}
