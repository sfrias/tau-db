package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import Enums.Tables;
import GUI.buttons.AutoCompleteComboBox;
import GUI.buttons.IconButton;
import GUI.layouts.RXCardLayout;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import db.DatabaseManager;

public abstract class GenericCardPanel extends JPanel implements GenericCardInerface{

	private static final long serialVersionUID = 1L;

	protected static DatabaseManager databaseManager = DatabaseManager.getInstance();
	protected AutoCompleteComboBox cb;
	protected final JTextField textName = new JTextField(20);
	
	protected boolean isSimpleCard;
	protected Tables table;
	JPanel fieldsNDefaultCards;
	private RXCardLayout cardLayout= new RXCardLayout();
	
	public static String DEFAULT_CARD = "default";
	public static String MAIN_CARD = "main";
	
	public GenericCardPanel(Tables table, boolean isSimpleCard){
		super();
		
		this.isSimpleCard = isSimpleCard;
		this.table = table;
		setLayout(new BorderLayout());
		
		if (isSimpleCard){
			addFields(null, null,null);
		}
		
		JPanel panelButton = new JPanel();
		IconButton buttonAction = new IconButton("OK","ok.png");
		buttonAction.addActionListener(createActionButtonListener());
		panelButton.add(buttonAction);
		
		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.PAGE_AXIS));
		panelBottom.add(new JSeparator(JSeparator.HORIZONTAL));
		panelBottom.add(panelButton);
		
		add(panelBottom,BorderLayout.SOUTH);
		
	}
	
	private String buildRowsSpecs(int nOfRows){
		int i;
		StringBuilder specs = new StringBuilder("");
		for (i=0 ; i<nOfRows ; i++ ){
			specs.append("p, 4dlu, ");
		}
		specs.append("p, 4dlu"); //one more for name field
		return specs.toString();
	}
	
	/** 
	 * no need to send "name" - adds automatic
	 */
	public void addFields(Vector<String> fieldsNames, Vector<JComponent> fieldsComponents, Vector<JComponent> extraFields) {
		assert ((fieldsNames==null && fieldsComponents==null && extraFields==null) ||
				(fieldsComponents.size()==fieldsNames.size() &&
				 fieldsComponents.size()==extraFields.size()));
		
		int numOfRows = fieldsNames==null ? 0 : fieldsNames.size();
		FormLayout layout = new FormLayout("left:pref, 4dlu, pref, 4dlu, left:pref", buildRowsSpecs(numOfRows));

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel(table.toString() + " name:", cc.xy(1,1));
		builder.add(textName, cc.xy(3,1));

		for (int i=0; i<numOfRows; i++){
			builder.addLabel(fieldsNames.remove(0) + ":",cc.xy(1, 2*i+3));
			builder.add(fieldsComponents.remove(0),cc.xy(3,2*i+3));
			JPanel extraField =(JPanel) extraFields.remove(0);
			if (extraField != null){
				builder.add(extraField, cc.xy(5, 2*i+3));
			}
		}
		
		fieldsNDefaultCards = new JPanel(cardLayout);
		
		fieldsNDefaultCards.add(new DefaultCard(),DEFAULT_CARD);
		fieldsNDefaultCards.add(builder.getPanel(),MAIN_CARD);
		
		if (fieldsNames!=null){  //not simple card
			JScrollPane panelScroll = new JScrollPane(fieldsNDefaultCards);
			add(panelScroll,BorderLayout.CENTER);
		} 
		else{
			add(fieldsNDefaultCards,BorderLayout.CENTER);
		}
		
	}
	
	public void switchCard(String cardName){
		if (cardName.compareTo(MAIN_CARD) == 0){
			cardLayout.show(fieldsNDefaultCards,MAIN_CARD);
		}else if (cardName.compareTo(DEFAULT_CARD)==0){
			cardLayout.show(fieldsNDefaultCards,DEFAULT_CARD);
		}else{
			System.out.println("no such card");
		}
	}
	
	public void refreshCards(){
		//createRecordCombo(false);
		generateRecords();
		textName.setText("");
		switchCard(DEFAULT_CARD);
	}


}

