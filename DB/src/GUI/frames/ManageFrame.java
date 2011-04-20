package GUI.frames;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import GUI.panels.Manage.Tabs.AddTab;
import GUI.utilities.GuiUtils;

public class ManageFrame extends JFrame {
	
	private JPanel cards = new JPanel(new CardLayout());
	
/*	public static void main(String[] args){
		TestFrame frame = new TestFrame(tabPanelBuilder());
		frame.display();
	}*/
	
	public ManageFrame(){
		buildFrame();
		GuiUtils.centerOnScreen(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt)
                {
                        backOperation();
                }
        });
	}
	
	private void buildFrame(){
		setTitle("Manage");
		setSize(600,600);
		setContentPane(tabPanelBuilder());
		setVisible(true);
	}
	
	
	private void backOperation(){
		WelcomeScreenFrame frame = new WelcomeScreenFrame();
		frame.setVisible(true);
		dispose();
	}
	
	private JTabbedPane tabPanelBuilder(){
		
/*		JPanel panelHead = new JPanel();
		panelHead.setLayout(new BoxLayout(panelHead, BoxLayout.PAGE_AXIS));
		panelHead.add(new JLabel("Please select a Category you wish to insert new record to:"));
		
		String[] categories = {"GENDER","NAMES"};
		AutoCompleteComboBox buttonCategory = new AutoCompleteComboBox(categories);
		buttonCategory.setPreferredSize(new Dimension(200,20));
		buttonCategory.addActionListener(this);
		
		JPanel panelTitle = new JPanel();
		panelTitle.add(buttonCategory);
		panelHead.add(panelTitle);
		panelHead.add(new JSeparator(JSeparator.HORIZONTAL));
		
		JPanel panelAdd = new JPanel(new BorderLayout());
		panelAdd.add(panelHead,BorderLayout.NORTH);
		
		addCards();
		panelAdd.add(cards,BorderLayout.CENTER);
		
		JPanel panelButton = new JPanel();
		JButton buttonAdd = new JButton("ADD");
		buttonAdd.addActionListener(new ActionListener() {
			//TODO WRITE ADD METHOD
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		panelButton.add(buttonAdd);
		
		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.PAGE_AXIS));
		panelBottom.add(new JSeparator(JSeparator.HORIZONTAL));
		panelBottom.add(panelButton);
		
		//panelAdd.add(panelBottom,BorderLayout.SOUTH);
*/		
		JTabbedPane tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("ADD", new AddTab());
		
		return tabbedPanel;
	}
	
/*	private  void addCards(){
		cards.add(new AddGender(), "GENDER");
		cards.add(new AddName(), "NAMES");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
        String cardName = (String)cb.getSelectedItem();
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, cardName);
		
	}*/
}
