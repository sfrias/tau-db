package GUI.panels.Manage.cards.add;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;

import Utils.Tables;

public class AddOccupation extends AddCard {
	private static final long serialVersionUID = 1L;
	
	public AddOccupation(){
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

}
