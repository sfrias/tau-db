package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import GUI.buttons.AutoCompleteComboBox;
import GUI.buttons.IconButton;
import GUI.commons.Pair;
import Utils.DatabaseManager;
import Utils.Tables;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public abstract class GenericCardPanel extends JPanel implements GenericCardInerface{
	private static final long serialVersionUID = 1L;

	protected static DatabaseManager databaseManager = DatabaseManager.getInstance();
	protected AutoCompleteComboBox cb;
	protected final JTextField textName = new JTextField(20);
	
	protected boolean isSimpleCard;
	protected Tables table;
	
	public GenericCardPanel(Tables table){
		this(table, true);
	}
	
	public GenericCardPanel(Tables table, boolean isSimpleCard){
		super();
		
		this.isSimpleCard = isSimpleCard;
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
		
		if (isSimpleCard){
			addFields(null, null);
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
	
	//no need to send "name" - automatic adds
	public void addFields(Vector<String> fieldsNames, Vector<JComponent> fieldsComponents) {
		assert ((fieldsNames==null && fieldsComponents==null) ||
				fieldsComponents.size()==fieldsNames.size());
		
		int numOfRows = fieldsNames==null ? 0 : fieldsNames.size();
		FormLayout layout = new FormLayout("right:pref, 4dlu, pref", buildRowsSpecs(numOfRows));

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel(table.toString() + " name:", cc.xy(1,1));
		builder.add(textName, cc.xy(3,1));

		for (int i=0; i<numOfRows; i++){
			builder.addLabel(fieldsNames.remove(0) + ":",cc.xy(1, 2*i+3));
			builder.add(fieldsComponents.remove(0),cc.xy(3,2*i+3));
		}
		
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
					textName.setText(valuesArr[0]);	
				}
			}
		};
	}

}
