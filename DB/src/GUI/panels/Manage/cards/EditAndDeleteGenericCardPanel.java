package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import enums.Tables;


import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.panels.Manage.cards.delete.DeleteCard;
import GUI.panels.Manage.cards.delete.DeleteCharacters;
import GUI.panels.Manage.cards.edit.EditCharacters;
import GUI.panels.Manage.cards.edit.EditSimpleCard;
import GUI.workers.GetRecordsByNameWorker;
import GUI.workers.GetSimpleRecordsWorker;

public abstract class EditAndDeleteGenericCardPanel extends GenericCardPanel implements EditAndDeleteGenericCardInteface{
	private static final long serialVersionUID = 1L;
	protected AutoCompleteComboBox comboRecord;
	private EditAndDeleteGenericCardPanel card = this;

	public EditAndDeleteGenericCardPanel(Tables table) throws Exception{
		this(table, true, true);
	}

	public EditAndDeleteGenericCardPanel(Tables table, boolean isSimpleCard){
		this(table, isSimpleCard, true);
	}

	public EditAndDeleteGenericCardPanel(Tables table, boolean isSimpleCard, boolean generateRecords){
		super(table, isSimpleCard);
		//Pair[] records = createRecordList();
		comboRecord = new AutoCompleteComboBox();
		comboRecord.setPrototypeDisplayValue("XXX");
		comboRecord.addActionListener(createRecordComboListener());
		comboRecord.setPreferredSize(new Dimension(200,20));
		final JPanel panelRecord = new JPanel();
		panelRecord.add(comboRecord);

		if (generateRecords) {
			generateRecords();
		}
		else{
			final JButton searchButton = new JButton("Search");
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					Document doc = ((JTextComponent)comboRecord.getEditor().getEditorComponent()).getDocument();
					String queryString;
					try {
						queryString = doc.getText(0, doc.getLength());
						GetRecordsByNameWorker worker;
						if (card instanceof EditCharacters){
							worker = new GetRecordsByNameWorker((EditCharacters) card, -1, queryString);
						}
						else{
							worker = new GetRecordsByNameWorker((DeleteCharacters) card, -1, queryString);
						}

						GuiHandler.startStatusFlash();
						worker.execute();
					} catch (BadLocationException e) {

						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			panelRecord.add(searchButton);


		}

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
					boolean refresh = !((card instanceof DeleteCharacters) || (card instanceof EditCharacters));
					if (refresh ){
						System.out.println("start cards refreshing in " + this + " " + getCardAction());
						refreshCards();

						textName.setText("");
					}
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
		else {
			worker = new GetSimpleRecordsWorker(table, (DeleteCard) this);

		}
		GuiHandler.startStatusFlash();
		worker.execute();
	}

}
