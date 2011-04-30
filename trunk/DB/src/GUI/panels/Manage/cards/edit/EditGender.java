package GUI.panels.Manage.cards.edit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
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
		
		FormLayout layout = new FormLayout("right:pref, 4dlu, pref", "p, 4dlu, p, 4dlu, p, 4dlu");
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel("Gender name:", cc.xy(1,1));
		builder.add(field1=new JTextField(17), cc.xy(3,1));
		
		add(builder.getPanel(),BorderLayout.CENTER);
	}

	public Pair[] createRecordList() {
		
		Pair [] valuesArr = databaseManager.executeQueryAndGetValues(Tables.gender, 3);
		return valuesArr;
	}

	@Override
	public ActionListener createRecordComboListener() { return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AutoCompleteComboBox cb = (AutoCompleteComboBox)e.getSource();
				Pair record = (Pair) cb.getSelectedItem();
				if (record != null){
					String [] valuesArr = databaseManager.getCurrentValues(Tables.gender, "gender_id", record.getId());
					assert (valuesArr.length == fieldsNum);
					field1.setText(valuesArr[0]);	
				}
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
