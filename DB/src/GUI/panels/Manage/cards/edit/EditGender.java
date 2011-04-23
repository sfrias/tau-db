package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionListener;

import javax.swing.JLabel;

public class EditGender extends abstractEditCard {
	public EditGender(){
		super();
	}

	@Override
	public void addFields() {
		add(new JLabel ("edit gender field"));
		
	}

	@Override
	public ActionListener setActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] createRecordList() {
		String[] result = {"editGender1", "editGender2"};
		return result;
	}

}
