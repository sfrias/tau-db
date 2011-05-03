package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import Utils.Tables;

public abstract class EditAndDeleteGenericCardPanel extends GenericCardPanel implements EditAndDeleteGenericCardInteface{
	private static final long serialVersionUID = 1L;
	
	public EditAndDeleteGenericCardPanel(Tables table){
		this(table, true);
	}
	
	public EditAndDeleteGenericCardPanel(Tables table, boolean isSimpleCard){
		
		super(table, isSimpleCard);
		
		Pair[] records = createRecordList();
		AutoCompleteComboBox comboRecord = new AutoCompleteComboBox(records);
		comboRecord.addActionListener(createRecordComboListener());
		comboRecord.setPreferredSize(new Dimension(200,20));
		//comboRecord.setSelectedIndex(0);
		
		JPanel panelHead = new JPanel();
		panelHead.setLayout(new BoxLayout(panelHead,BoxLayout.PAGE_AXIS));
		
		JPanel panelRecord = new JPanel();
		panelRecord.add(comboRecord);
		panelHead.add(new JLabel("please select a record:"));
		panelHead.add(panelRecord);
		panelHead.add(new JSeparator(JSeparator.HORIZONTAL));
		add(panelHead,BorderLayout.NORTH);
	}

	public Pair[] createRecordList() {

		Pair [] valuesArr = databaseManager.executeQueryAndGetValues(table, 3);
		return valuesArr;
	}
	
	public ActionListener createRecordComboListener() { 

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cb = (AutoCompleteComboBox)e.getSource();
				Pair record = (Pair) cb.getSelectedItem();
				if (record != null){
					String [] valuesArr = databaseManager.getCurrentValues(table, table.toString()+"_id", record.getId());
					textName.setText(valuesArr[0]);
				}
			}
		};
	}

}
