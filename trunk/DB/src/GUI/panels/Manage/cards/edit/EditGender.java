package GUI.panels.Manage.cards.edit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import GUI.buttons.AutoCompleteComboBox;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class EditGender extends abstractEditCard {
	private JTextField field1;
	private JTextField field2;
	private JTextField field3;
	
	
	public EditGender(){
		super();
	}

	@Override
	public void addFields() {
		JPanel panelFields = new JPanel();
		
		FormLayout layout = 
			new FormLayout("right:pref, 4dlu, pref",
				"p, 4dlu, p, 4dlu, p, 4dlu");
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel("Field 1:", cc.xy(1,1));
		builder.add(field1=new JTextField(17), cc.xy(3,1));
		builder.addLabel("Field 2:", cc.xy(1,3));
		builder.add(field2=new JTextField(17), cc.xy(3,3));
		builder.addLabel("Field 3:", cc.xy(1,5));
		builder.add(field2=new JTextField(17), cc.xy(3,5));
		
		add(builder.getPanel(),BorderLayout.CENTER);
	}

	@Override
	public String[] createRecordList() {
		String[] result = {"editGender1", "editGender2"};
		return result;
	}

	@Override
	public ActionListener createRecordComboListener() { 
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AutoCompleteComboBox cb = (AutoCompleteComboBox)e.getSource();
				String recordName = (String) cb.getSelectedItem();
			}
		};
	}
	
	@Override
	public ActionListener createActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
