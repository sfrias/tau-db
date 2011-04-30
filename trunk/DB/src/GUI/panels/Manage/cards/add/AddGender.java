package GUI.panels.Manage.cards.add;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import GUI.commons.Pair;
import Utils.Tables;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class AddGender extends abstractAddCard {
	/**
	 * 
	 */
	private static final long serialVersionUID = -255349874072694557L;
	private JTextField field1;
	private JTextField field2;
	
	public AddGender(){
		super(Tables.gender);
	}

	@Override
	public void addFields() {
		JPanel panelFields = new JPanel();
		
		FormLayout layout = 
			new FormLayout("right:pref, 4dlu, pref", "p, 4dlu, p, 4dlu");
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel("Field 1:", cc.xy(1,1));
		builder.add(field1=new JTextField(17), cc.xy(3,1));
		builder.addLabel("Field 2:", cc.xy(1,3));
		builder.add(field2=new JTextField(17), cc.xy(3,3));
		
		/*panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.PAGE_AXIS));
		
		JPanel panelLine;
		JLabel labelName = new JLabel("Name: ");
		JTextField textName = new JTextField();
		
		panelLine = new JPanel();
		panelLine.setLayout(new BoxLayout(panelLine, BoxLayout.LINE_AXIS));
		panelLine.add(labelName);
		panelLine.add(textName);
		
		panelFields.add(panelLine);
		*/
		//panelFields.setLayout(layout);
		add(builder.getPanel(),BorderLayout.CENTER);
	}

	@Override
	public ActionListener createActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair[] createRecordList() {
		Pair [] result = {new Pair("record1",1), new Pair("record2",2)};
		return result;
	}

	@Override
	public ActionListener createRecordComboListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
