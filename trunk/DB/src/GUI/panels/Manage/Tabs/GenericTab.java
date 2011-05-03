package GUI.panels.Manage.Tabs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import Utils.Tables;

import GUI.buttons.AutoCompleteComboBox;
import GUI.panels.Manage.cards.edit.EditCreator;
import GUI.panels.Manage.cards.edit.EditDiseases;
import GUI.panels.Manage.cards.edit.EditEthnicity;
import GUI.panels.Manage.cards.edit.EditGender;
import GUI.panels.Manage.cards.edit.EditJob;
import GUI.panels.Manage.cards.edit.EditLocation;
import GUI.panels.Manage.cards.edit.EditOccupation;
import GUI.panels.Manage.cards.edit.EditPower;
import GUI.panels.Manage.cards.edit.EditRank;
import GUI.panels.Manage.cards.edit.EditSchool;
import GUI.panels.Manage.cards.edit.EditSpecies;
import GUI.panels.Manage.cards.edit.EditUniverse;

public abstract class GenericTab extends JPanel implements ActionListener, GenericTabInterface{
	private static final long serialVersionUID = 1L;
	
	private JPanel cards ;
	
	public GenericTab(){
		super();
		
		JPanel panelHead = new JPanel();
		panelHead.setLayout(new BoxLayout(panelHead, BoxLayout.PAGE_AXIS));
		panelHead.add(new JLabel("Please select a Category you wish to " + getTabAction() + " new record to:"));
		
		//TODO insert all tabels names!!!!
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
		add(cards,BorderLayout.CENTER);	
	}
	
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
        String cardName = (String)cb.getSelectedItem();
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, cardName);
		
	}
	
}

