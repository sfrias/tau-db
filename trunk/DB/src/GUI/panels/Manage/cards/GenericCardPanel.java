package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import Utils.DatabaseManager;

import GUI.buttons.AutoCompleteComboBox;

public abstract class GenericCardPanel extends JPanel implements GenericCardInerface{
	
	protected DatabaseManager databaseManager = DatabaseManager.getInstance();
	
	public GenericCardPanel(){
		super();
		setLayout(new BorderLayout());
		
		String[] records = createRecordList();
		AutoCompleteComboBox comboRecord = new AutoCompleteComboBox(records);
		comboRecord.addActionListener(createRecordComboListener());
		comboRecord.setPreferredSize(new Dimension(200,20));
		
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

}
