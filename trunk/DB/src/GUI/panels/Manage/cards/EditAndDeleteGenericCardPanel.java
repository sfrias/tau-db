package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.panels.Manage.cards.delete.DeleteCard;
import GUI.panels.Manage.cards.edit.EditCharacters;
import GUI.panels.Manage.cards.edit.EditSimpleCard;
import GUI.workers.GetSimpleRecordsWorker;

public abstract class EditAndDeleteGenericCardPanel extends GenericCardPanel implements EditAndDeleteGenericCardInteface{
	private static final long serialVersionUID = 1L;
	protected AutoCompleteComboBox comboRecord;

	public EditAndDeleteGenericCardPanel(Tables table) throws Exception{
		this(table, true);
	}

	public EditAndDeleteGenericCardPanel(Tables table, boolean isSimpleCard){
		super(table, isSimpleCard);
		//Pair[] records = createRecordList();
		comboRecord = new AutoCompleteComboBox();
		comboRecord.setPrototypeDisplayValue("XXX");
		comboRecord.addActionListener(createRecordComboListener());
		comboRecord.setPreferredSize(new Dimension(200,20));
		JPanel panelRecord = new JPanel();
		panelRecord.add(comboRecord);
		
		//createRecordCombo(true); //inserts combo into panelRecord
		generateRecords();
		//comboRecord.setSelectedIndex(0);

		JPanel panelHead = new JPanel();
		panelHead.setLayout(new BoxLayout(panelHead,BoxLayout.PAGE_AXIS));


		panelHead.add(panelRecord);
		panelHead.add(new JSeparator(JSeparator.HORIZONTAL));
		add(panelHead,BorderLayout.NORTH); 

		addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {				
			}

			@Override
			public void focusGained(FocusEvent e) {
				try {
					System.out.println("start cards refreshing in " + this + " " + getCardAction());
					refreshCards();

					textName.setText("");/*
					switchCard(DEFAULT_CARD);*/
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	public void generateRecords(){
		GetSimpleRecordsWorker worker;

		if (this instanceof EditSimpleCard){
			worker = new GetSimpleRecordsWorker(table, (EditSimpleCard) this);
		}
		else if(this instanceof DeleteCard){
			worker = new GetSimpleRecordsWorker(table, (DeleteCard) this);

		}
		else{
			worker = new GetSimpleRecordsWorker(table, (EditCharacters) this);
		}
		GuiHandler.startStatusFlash();
		worker.execute();
	}

}
