package GUI.panels.Manage.cards.delete;

import java.awt.event.ActionListener;

import javax.swing.JLabel;

import GUI.commons.Pair;
import GUI.panels.Manage.cards.edit.abstractEditCard;

public class DeleteGender extends abstractDeleteCard {
	public DeleteGender(){
		super();
	}

	@Override
	public void addFields() {
		add(new JLabel ("delete gender field"));
		
	}

	@Override
	public ActionListener createActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair[] createRecordList() {
		Pair[] result = {new Pair("deleteName1",1), new Pair("deleteName2",2)};
		return result;
	}

	@Override
	public ActionListener createRecordComboListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
