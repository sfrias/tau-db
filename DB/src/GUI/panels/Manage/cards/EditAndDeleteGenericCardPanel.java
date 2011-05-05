package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import GUI.frames.WelcomeScreenFrame;
import GUI.workers.GetSimpleRecordsWorker;

public abstract class EditAndDeleteGenericCardPanel extends GenericCardPanel implements EditAndDeleteGenericCardInteface{
	private static final long serialVersionUID = 1L;
	private JPanel panelRecord = new JPanel();
	private AutoCompleteComboBox comboRecord;
	protected EditAndDeleteGenericCardPanel thisCard; 
	
	public EditAndDeleteGenericCardPanel(Tables table){
		this(table, true);
	}
	
	public EditAndDeleteGenericCardPanel(Tables table, boolean isSimpleCard){
		super(table, isSimpleCard);
		
		thisCard = this;
		/*Pair[] records = createRecordList();
		AutoCompleteComboBox comboRecord = new AutoCompleteComboBox(records);
		comboRecord.addActionListener(createRecordComboListener());
		comboRecord.setPreferredSize(new Dimension(200,20));*/
	
		//comboRecord.setSelectedIndex(0);
		
		JPanel panelHead = new JPanel();
		panelHead.setLayout(new BoxLayout(panelHead,BoxLayout.PAGE_AXIS));
		
		try {
			createRecordCombo(true); //inserts combo into panelRecord
			
			panelHead.add(new JLabel("please select a record:"));
			panelHead.add(panelRecord);
			panelHead.add(new JSeparator(JSeparator.HORIZONTAL));
			add(panelHead,BorderLayout.NORTH);
		} catch (Exception e) {
			GuiHandler.ShowErrorGetRecords();
        	GuiHandler.switchFrames(new WelcomeScreenFrame());
		} 
		
	}

	public Pair[] createRecordList() throws Exception{
		GetSimpleRecordsWorker worker = new GetSimpleRecordsWorker(table);
		GuiHandler.startStatusFlash();
		worker.execute();
		try {
			Pair[] result = worker.get(10,TimeUnit.MILLISECONDS);
			GuiHandler.stopStatusFlash();
			if (result != null){
				return result;
			} 
		} 
		catch (InterruptedException e) {}
		catch (ExecutionException e) {}
		catch (TimeoutException e) {}
		
		throw new Exception();
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
	
	private void createRecordCombo(boolean isFirstCreation) throws Exception{
		if (!isFirstCreation){
			panelRecord.remove(comboRecord);
		}
		Pair[] records = createRecordList();
		
		comboRecord = new AutoCompleteComboBox(records);
		comboRecord.addActionListener(createRecordComboListener());
		comboRecord.setPreferredSize(new Dimension(200,20));
		panelRecord.add(comboRecord,0);
		panelRecord.validate();
	}
	
	public void refreshCards() throws Exception{
		JPanel parent = (JPanel) getParent();
		CardLayout layout = (CardLayout)parent.getLayout();
		layout.show(parent,"default");
		createRecordCombo(false);
	}
	
}
