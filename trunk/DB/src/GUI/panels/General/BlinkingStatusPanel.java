package GUI.panels.General;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class BlinkingStatusPanel extends JPanel{
	private static final long serialVersionUID = 1L;
		
	private JLabel labelStatus;
	private Color visibleColor;
	private Color inVisibleColor;
		
	private Timer timer = new Timer(100, new ActionListener() {
		private boolean on = false;
		@Override
		public void actionPerformed(ActionEvent e) {
			flash(on);
			on = !on;
		}
	});
	
	public BlinkingStatusPanel(){
		super();
		
		Font font = new Font("Footlight MT Light", Font.BOLD, 18);
		visibleColor = Color.BLACK;
		inVisibleColor = getBackground();
		
		JLabel labelTitle = new JLabel("Status:");
		labelTitle.setFont(font);
		labelStatus = new JLabel("Ready");
		labelStatus.setFont(font);
		labelStatus.setForeground(Color.BLACK);
		setBorder(new EmptyBorder(5,0,0,0));
		add(labelTitle);
		add(labelStatus);
		
	}
		
	
		public void flash() {
			timer.start();
		}

		private void flash(boolean on) {
			if (on) {
				labelStatus.setForeground(visibleColor);
			} else {
				labelStatus.setForeground(inVisibleColor);
			}
		}

		public void clearFlashing() {
			timer.stop();
			labelStatus.setVisible(true);
		}
		
}
