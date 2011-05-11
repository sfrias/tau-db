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
import GUI.commons.Pair;
import GUI.model.SimpleModel;
import GUI.workers.GetSimpleRecordsWorker;

public abstract class EditAndDeleteGenericCardPanel extends GenericCardPanel implements EditAndDeleteGenericCardInteface{
	private static final long serialVersionUID = 1L;
	private AutoCompleteComboBox comboRecord;
	protected EditAndDeleteGenericCardPanel thisCard;
	private SimpleModel model;

	public EditAndDeleteGenericCardPanel(Tables table) throws Exception{
		this(table, true);
	}

	public EditAndDeleteGenericCardPanel(Tables table, boolean isSimpleCard) throws Exception{
		super(table, isSimpleCard);
		//Pair[] records = createRecordList();
		comboRecord = new AutoCompleteComboBox();
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
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

/*	public Pair[] createRecordList() throws Exception{
		GetSimpleRecordsWorker worker = new GetSimpleRecordsWorker(table, this);
		GuiHandler.startStatusFlash();
		worker.execute();
		try {
			Pair[] result = worker.get(10000,TimeUnit.MILLISECONDS);
			GuiHandler.stopStatusFlash();
			if (result != null){
				return result;
			} 
		} 
		catch (InterruptedException e) {}
		catch (ExecutionException e) {}
		catch (TimeoutException e) {}

		throw new Exception();
	}*/

/*	private void createRecordCombo(boolean isFirstCreation) throws Exception{
		if (!isFirstCreation){
			panelRecord.remove(comboRecord);
		}
		Pair[] records = createRecordList();

		comboRecord = new AutoCompleteComboBox(records);
		comboRecord.addActionListener(createRecordComboListener());
		comboRecord.setPreferredSize(new Dimension(200,20));
		panelRecord.add(comboRecord,0);
		panelRecord.validate();
	}*/

	public void generateRecords(){
		GetSimpleRecordsWorker worker = new GetSimpleRecordsWorker(table, this);
		GuiHandler.startStatusFlash();
		worker.execute();
	}
	
	public void refreshFromModel(){
		comboRecord.removeAllItems();
		Pair[] pairs = model.getRecords();
		for (int i=0; i<pairs.length; i++){
			comboRecord.addItem(pairs[i]);
		}
		
		if (GuiHandler.isStatusFlashing()){
			GuiHandler.stopStatusFlash();
		}
	}
	
	
	public void setModel(SimpleModel model){
		this.model = model;
	}
	
	public SimpleModel getModel(){
		return model;
	}
	


}
