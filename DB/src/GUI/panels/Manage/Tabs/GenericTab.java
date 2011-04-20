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

import GUI.buttons.AutoCompleteComboBox;
import GUI.panels.Manage.generalTabPanel;

public abstract class GenericTab extends JPanel implements ActionListener, generalTabPanel{
	
	private JPanel cards ;
	
	public GenericTab(){
		super();
		
		JPanel panelHead = new JPanel();
		panelHead.setLayout(new BoxLayout(panelHead, BoxLayout.PAGE_AXIS));
		panelHead.add(new JLabel("Please select a Category you wish to "+getTabAction()+" new record to:"));
		
		String[] categories = {"GENDER","NAMES"};
		AutoCompleteComboBox buttonCategory = new AutoCompleteComboBox(categories);
		buttonCategory.setPreferredSize(new Dimension(200,20));
		buttonCategory.addActionListener(this);
		
		JPanel panelTitle = new JPanel();
		panelTitle.add(buttonCategory);
		panelHead.add(panelTitle);
		panelHead.add(new JSeparator(JSeparator.HORIZONTAL));
		
		setLayout(new BorderLayout());
		add(panelHead,BorderLayout.NORTH);
		
		cards = new JPanel(new CardLayout());
		cards = addCards(cards);
		add(cards,BorderLayout.CENTER);
		
		/*JPanel panelButton = new JPanel();
		JButton buttonAction = new JButton(getTabAction().toUpperCase());
		panelButton.add(buttonAction);
		
		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.PAGE_AXIS));
		panelBottom.add(new JSeparator(JSeparator.HORIZONTAL));
		panelBottom.add(panelButton);*/
	}
	
/*	public void initializeCategoryComboBox(String[] categories){
		ComboCategory = new JComboBox(categories);
	}
	
	public JComboBox getCategoryComboBox(){
		return ComboCategory;
	}*/
	
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
        String cardName = (String)cb.getSelectedItem();
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, cardName);
		
	}
	
}

