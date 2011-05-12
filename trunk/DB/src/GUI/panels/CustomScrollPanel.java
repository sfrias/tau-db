package GUI.panels;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;

public class CustomScrollPanel extends JScrollPane{
	private static final long serialVersionUID = 1L;

	public CustomScrollPanel(int width, int height){
		super();		
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setPreferredSize(new Dimension(width,height));
	}
	
	public CustomScrollPanel(Component view, int width, int height){
		super(view);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setPreferredSize(new Dimension(width,height));
	}

}
