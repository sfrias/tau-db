package GUI.test;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class TestFrame extends JFrame {

        // Create and set up the window
        public TestFrame(JComponent component) {                
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                getContentPane().add(component);
        }
        
        // Display the window
        public void display() {
        		setSize(600,600);
                setResizable(false);
                pack();
                setVisible(true);
        }
        
}
