package GUI.list;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JList;

public class DisplayList extends JList {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DisplayList() {
		super();
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setPreferredSize(new Dimension(150, 50));
	}

}
