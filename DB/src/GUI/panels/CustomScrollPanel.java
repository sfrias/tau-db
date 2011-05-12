package GUI.panels;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;

public class CustomScrollPanel extends JScrollPane{
	private static final long serialVersionUID = 1L;

	public CustomScrollPanel(){
		super();		
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setPreferredSize(new Dimension(150,100));
	}
	
	public CustomScrollPanel(Component view){
		super(view);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setPreferredSize(new Dimension(150,100));
	}

}
