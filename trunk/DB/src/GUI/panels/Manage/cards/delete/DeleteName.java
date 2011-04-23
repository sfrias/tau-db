package GUI.panels.Manage.cards.delete;

import java.awt.event.ActionListener;

import javax.swing.JLabel;

public class DeleteName extends abstractDeleteCard {
	public DeleteName(){
		super();
		
	}

	@Override
	public void addFields() {
		add(new JLabel("edit name field"));
		
	}

	@Override
	public ActionListener setActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
