package GUI.panels.Manage.cards.edit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import GUI.buttons.AutoCompleteComboBox;
import GUI.frames.PlayFrame;
import Utils.DatabaseManager;
import Utils.Tables;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class EditGender extends abstractEditCard {
	
	private JTextField field1;
	
	public EditGender(){
		super();
		fieldsNum = 1;
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
		
		add(builder.getPanel(),BorderLayout.CENTER);
	}

	@Override
	public String[] createRecordList() {
		
		String [] valuesArr = databaseManager.executeQueryAndGetValues(Tables.gender, 3);
		return valuesArr;
	}

	@Override
	public ActionListener createRecordComboListener() { return new ActionListener() {
			

			@Override
			public void actionPerformed(ActionEvent e) {
				AutoCompleteComboBox cb = (AutoCompleteComboBox)e.getSource();
				String recordName = (String) cb.getSelectedItem();
				String [] valuesArr = databaseManager.getCurrentValues(Tables.gender, recordName);
				assert (valuesArr.length == fieldsNum);
				field1.setText(valuesArr[0]);	
			}
		};
	}
	
	@Override
	public ActionListener createActionButtonListener() {
		return new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						//TODO HILA CODE HERE
					}
				});
			}
		};
	}
}
