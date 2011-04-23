package GUI.panels.Manage.cards.add;

import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class AddName extends abstractAddCard {
	public AddName(){
		super();
	}

	@Override
	public void addFields() {
		add(new JLabel("name field"));
		
	}

	@Override
	public ActionListener setActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
