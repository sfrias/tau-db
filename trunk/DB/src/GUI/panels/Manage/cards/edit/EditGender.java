package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionListener;

import javax.swing.JLabel;

public class EditGender extends abstractEditCard {
	public EditGender(){
		super();/*
		setLayout(new BorderLayout());
		add(new JLabel ("Gender Details"),BorderLayout.CENTER);
		
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
		
		add(panelBottom,BorderLayout.SOUTH);*/
	}

	@Override
	public void addFields() {
		add(new JLabel ("edit gender field"));
		
	}

	@Override
	public ActionListener setActionButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
