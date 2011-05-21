package GUI.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class CustomGlassPane extends JComponent implements MouseInputListener{
	private static final long serialVersionUID = 1L;

	//components which are allowed to be interactive when glassPane is visible - can be null.
	private JComponent[] interactiveComponents;  
	private Container contentPane;

	
	public CustomGlassPane(JComponent[] interactiveComponents, Container contentPane){
		super();
		this.interactiveComponents = interactiveComponents;
		this.contentPane = contentPane;
		
		setBackground(new Color(0,0,0,0)); //makes it transparent
		addMouseListener(this);
		addMouseMotionListener(this);	
		
		setVisible(false);
	}

	public void mouseClicked(MouseEvent e) {
		dispatchComponent(e);
	}
	public void mouseEntered(MouseEvent e) {
		dispatchComponent(e);
	}
	public void mouseExited(MouseEvent e) {
		dispatchComponent(e);
	}
	public void mousePressed(MouseEvent e) {
		dispatchComponent(e);
	}
	public void mouseReleased(MouseEvent e) {
		dispatchComponent(e);
	}
	public void mouseDragged(MouseEvent e) {
		dispatchComponent(e);
	}
	public void mouseMoved(MouseEvent e) {
		dispatchComponent(e);
	}

	private void dispatchComponent(MouseEvent e){
		Point glassPoint = e.getPoint();
		Point containerPoint = SwingUtilities.convertPoint(this,glassPoint,contentPane);

		Component component = SwingUtilities.getDeepestComponentAt(contentPane, containerPoint.x, containerPoint.y);

		if (component !=null && isInteractive(component)){
			Point componentPoint = SwingUtilities.convertPoint(this,glassPoint,component);
			component.dispatchEvent(new MouseEvent(component,
					e.getID(),
					e.getWhen(),
					e.getModifiers(),
					componentPoint.x,
					componentPoint.y,
					e.getClickCount(),
					e.isPopupTrigger()));
		}

	}
	
	private boolean isInteractive(Component component){
		if (interactiveComponents==null){
			return false;
		}else{
			for (int i=0; i<interactiveComponents.length; i++){
				if (interactiveComponents[i].equals(component)){
					return true;
				}
			}
			return false;
		}
	}


}
