package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionListener;

import javax.swing.JLabel;

import GUI.commons.Pair;

public class EditName extends abstractEditCard {
	public EditName(){
		super();
	}

	@Override
	public void addFields() {
		add(new JLabel ("edit name field"));
		
	}

	@Override
	public ActionListener createActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair[] createRecordList() {
		Pair [] result = {new Pair("editName1",1), new Pair("editName2",2)};
		return result;
	}

	@Override
	public ActionListener createRecordComboListener() {
		// TODO Auto-generated method stub
		return null;
	}

}