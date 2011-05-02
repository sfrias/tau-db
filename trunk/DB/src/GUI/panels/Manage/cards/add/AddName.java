package GUI.panels.Manage.cards.add;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;

import Utils.Tables;
import GUI.commons.Pair;

public class AddName extends abstractAddCard {
	private static final long serialVersionUID = 1L;
	
	public AddName(){
		super(Tables.occupation);
	}

	@Override
	public void addFields(Vector<String> fieldsNames, Vector<JComponent> fieldsComponents) {
		add(new JLabel("name field"));
		
	}

	@Override
	public ActionListener createActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair [] createRecordList() {
		Pair[] result = {new Pair("addName1",1), new Pair("addName2",2)};
		return result;
	}

	@Override
	public ActionListener createRecordComboListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
