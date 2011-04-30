package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import Utils.DatabaseManager;
import Utils.Tables;

import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;

public abstract class GenericCardPanel extends JPanel implements GenericCardInerface{

	private static final long serialVersionUID = 4075894162742374973L;
	
	protected static DatabaseManager databaseManager = DatabaseManager.getInstance();
	protected AutoCompleteComboBox cb;
	protected Tables table;
	protected JTextField field1;

	public GenericCardPanel(Tables table){
		super();
		
		this.table = table;
		setLayout(new BorderLayout());
		
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
		
		addFields();
		
		JPanel panelButton = new JPanel();
		JButton buttonAction = new JButton(getCardAction());
		buttonAction.addActionListener(createActionButtonListener());
		panelButton.add(buttonAction);
		
		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.PAGE_AXIS));
		panelBottom.add(new JSeparator(JSeparator.HORIZONTAL));
		panelBottom.add(panelButton);
		
		add(panelBottom,BorderLayout.SOUTH);
		
	}
	
	@Override
	public void addFields() {

		JPanel panelFields = new JPanel();

		FormLayout layout = new FormLayout("right:pref, 4dlu, pref", "p, 4dlu, p, 4dlu, p, 4dlu");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel(table.toString() + " name:", cc.xy(1,1));
		builder.add(field1 = new JTextField(17), cc.xy(3,1));

		add(builder.getPanel(),BorderLayout.CENTER);
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
					field1.setText(valuesArr[0]);	
				}
			}
		};
	}

}
