package GUI.panels.Manage.Tabs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import Enums.Tables;
import GUI.buttons.AutoCompleteComboBox;

public abstract class GenericTab extends JPanel implements ActionListener, GenericTabInterface{
	private static final long serialVersionUID = 1L;
	
	private JPanel cards ;
	
	public GenericTab() throws Exception{
		super();
		
		JPanel panelHead = new JPanel();
		panelHead.setLayout(new BoxLayout(panelHead, BoxLayout.PAGE_AXIS));
		panelHead.add(new JLabel("Please select a Category you wish to " + getTabAction() + " new record to:"));
		
		//TODO insert all tabels names!!!!
		//String[] categories = createAllTablesArray();
		String[] categories = {Tables.creator.toString().toUpperCase(), Tables.disease.toString().toUpperCase(), Tables.ethnicity.toString().toUpperCase(),
		Tables.gender.toString().toUpperCase(), Tables.job.toString().toUpperCase(), Tables.location.toString().toUpperCase(),
		Tables.occupation.toString().toUpperCase(), Tables.organization.toString().toUpperCase(), Tables.power.toString().toUpperCase(), Tables.rank.toString().toUpperCase(),
		Tables.school.toString().toUpperCase(),  Tables.species.toString().toUpperCase(),Tables.universe.toString().toUpperCase() };
		
		AutoCompleteComboBox comboCategory = new AutoCompleteComboBox(categories);
		comboCategory.setPreferredSize(new Dimension(200,20));
		comboCategory.addActionListener(this);
		
		JPanel panelTitle = new JPanel();
		panelTitle.add(comboCategory);
		panelHead.add(panelTitle);
		panelHead.add(new JSeparator(JSeparator.HORIZONTAL));
		
		setLayout(new BorderLayout());
		add(panelHead,BorderLayout.NORTH);
		
		cards = new JPanel(new CardLayout());
		cards = addCards(cards);
		
		if (cards != null){
			add(cards,BorderLayout.CENTER);	
		}
		else{
			throw new Exception();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
        String cardName = (String)cb.getSelectedItem();
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, cardName);
	}
	
	private String[] createAllTablesArray(){
		Tables[] tables = Tables.values(); 
		List<String> resultList = new ArrayList<String>();
		for (Tables t : tables){
			resultList.add(t.toString().toUpperCase());			
		}
		Object[] objArr = resultList.toArray();
		return Arrays.copyOf(objArr, objArr.length, String[].class);
		
		/*return {Tables.creator.toString().toUpperCase(), Tables.disease.toString().toUpperCase(), Tables.ethnicity.toString().toUpperCase(),
				Tables.gender.toString().toUpperCase(), Tables.job.toString().toUpperCase(), Tables.location.toString().toUpperCase(),
				Tables.occupation.toString().toUpperCase(), Tables.organization.toString().toUpperCase(), Tables.power.toString().toUpperCase(), Tables.rank.toString().toUpperCase(),
				Tables.school.toString().toUpperCase(),  Tables.species.toString().toUpperCase(),Tables.universe.toString().toUpperCase() };*/
	}
	
}

