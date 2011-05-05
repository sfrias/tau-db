package GUI.panels.Manage.cards.add;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Enums.Tables;

public class AddCharacters extends AddCard {
	private static final long serialVersionUID = 1L;
	
	private JTextField field= new JTextField(20);
	private JTextField addField = new JTextField(20);
	
	Vector<String> titles = new Vector<String>();
	Vector<JComponent> components = new Vector<JComponent>();
	Vector<JComponent> extraAddPanels = new Vector<JComponent>();

	public AddCharacters(){
		super(Tables.characters, false);
		populateVectors();

		addFields(titles, components, extraAddPanels);
	}
	
	private void populateVectors(){
		addEntries("test",field,addField);
	}
	
	private void addEntries(String title, JTextField field, JTextField addField){
		final JTextField newField = addField;
		
		titles.add(title);
		components.add(field);
		if (addField != null){
			JButton button = new JButton("add");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					newField.setVisible(!newField.isVisible());
				}
			});
			JPanel panel = new JPanel();
			panel.add(button);
			panel.add(newField);
			extraAddPanels.add(panel);
		} else{
			extraAddPanels.add(null);
		}
	}
}
