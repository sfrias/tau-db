package GUI.panels.Manage.cards.delete;

import java.awt.event.ActionListener;

import javax.swing.JLabel;

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
	public ActionListener setActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
