package GUI.panels.Manage.cards.add;

import java.awt.event.ActionListener;
import javax.swing.JLabel;
import GUI.commons.Pair;

public class AddName extends abstractAddCard {
	public AddName(){
		super();
	}

	@Override
	public void addFields() {
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