package GUI.panels.Manage.Tabs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import GUI.buttons.AutoCompleteComboBox;
import GUI.layouts.RXCardLayout;
import enums.Tables;

public abstract class GenericTab extends JPanel implements ActionListener, GenericTabInterface{
	private static final long serialVersionUID = 1L;

	private JPanel cards ;

	public GenericTab(){
		super();

		JPanel panelHead = new JPanel();
		panelHead.setLayout(new BoxLayout(panelHead, BoxLayout.PAGE_AXIS));
		panelHead.add(new JLabel("Please select a Category you wish to " + getTabAction() + " new record to:"));

		//TODO insert all tabels names!!!!
		//String[] categories = createAllTablesArray();
		String[] categories = {Tables.characters.toString().toUpperCase(), Tables.disease.toString().toUpperCase(),
				Tables.occupation.toString().toUpperCase(), Tables.organization.toString().toUpperCase(), 
				Tables.power.toString().toUpperCase(), Tables.place_of_birth.toString().toUpperCase(), Tables.school.toString().toUpperCase() ,Tables.universe.toString().toUpperCase() };

		AutoCompleteComboBox comboCategory = new AutoCompleteComboBox(categories);
		comboCategory.setPreferredSize(new Dimension(200,20));
		comboCategory.addActionListener(this);

		JPanel panelTitle = new JPanel();
		panelTitle.add(comboCategory);
		panelHead.add(panelTitle);
		panelHead.add(new JSeparator(JSeparator.HORIZONTAL));

		setLayout(new BorderLayout());
		add(panelHead,BorderLayout.NORTH);

		//cards = new JPanel(new CardLayout());
		cards = new JPanel(new RXCardLayout());
		cards = addCards(cards);
		add(cards,BorderLayout.CENTER);	
		comboCategory.setSelectedIndex(0);

	}

	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
		String cardName = (String)cb.getSelectedItem();
		RXCardLayout cl = (RXCardLayout)(cards.getLayout());
		cl.show(cards, cardName);
/*		GenericCardPanel card = (GenericCardPanel)cl.getCurrentCard();
		card.switchCard(GenericCardPanel.MAIN_CARD);*/
	}

}

