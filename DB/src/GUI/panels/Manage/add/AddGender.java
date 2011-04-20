package GUI.panels.Manage.add;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class AddGender extends JPanel {
	public AddGender(){
		super();
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
		
		add(panelBottom,BorderLayout.SOUTH);
	}

}
