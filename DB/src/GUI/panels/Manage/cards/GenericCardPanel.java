package GUI.panels.Manage.cards;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import GUI.buttons.AutoCompleteComboBox;
import GUI.buttons.IconButton;
import GUI.panels.Manage.cards.delete.DeleteCard;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import enums.Tables;

public abstract class GenericCardPanel extends JPanel implements GenericCardInerface{

	private static final long serialVersionUID = 1L;

	protected AutoCompleteComboBox cb;
	protected final JTextField textName = new JTextField(20);
	
	protected boolean isSimpleCard;
	protected Tables table;
	
	public static String DEFAULT_CARD = "default";
	public static String MAIN_CARD = "main";
	
	public GenericCardPanel(Tables table, boolean isSimpleCard){
		super();
		
		this.isSimpleCard = isSimpleCard;
		this.table = table;
		setLayout(new BorderLayout());
		
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
			specs.append("top:p, 4dlu, ");
		}
		specs.append("top:p, 4dlu"); //one more for name field
		return specs.toString();
	}
	
	/** 
	 * no need to send "name" - adds automatic
	 */
	public void addFields(Vector<String> fieldsNames, Vector<JComponent> fieldsComponents) {
		assert ((fieldsNames==null && fieldsComponents==null) || (fieldsComponents.size()==fieldsNames.size()));
		
		int numOfRows = fieldsNames==null ? 0 : fieldsNames.size();
		FormLayout layout = new FormLayout("left:pref, 4dlu, pref", buildRowsSpecs(numOfRows));

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		if (!(this instanceof DeleteCard)){
			JLabel label = new JLabel(table.toString() + " name:");
			label.setFont(new Font("Footlight MT Light", Font.PLAIN, 15));
			builder.add(label, cc.xy(1,1));
			builder.add(textName, cc.xy(3,1));
		}
		for (int i=0; i<numOfRows; i++){
			JLabel fieldName = new JLabel(fieldsNames.remove(0) + ":");
			fieldName.setFont(new Font("Footlight MT Light", Font.PLAIN, 15));
			builder.add(fieldName,cc.xy(1, 2*i+3));
			builder.add(fieldsComponents.remove(0),cc.xy(3,2*i+3));
		}
		
		if (fieldsNames!=null){  //not simple card
			JScrollPane panelScroll = new JScrollPane(builder.getPanel());
			panelScroll.setWheelScrollingEnabled(true);
			panelScroll.getVerticalScrollBar().setUnitIncrement(20);
			add(panelScroll,BorderLayout.CENTER);
		} 
		else{
			add(builder.getPanel(),BorderLayout.CENTER);
		}
		
	}
	
	public void refreshCards(){
		generateRecords();
	}


}

