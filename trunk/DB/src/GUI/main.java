package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;

public class main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabelWelcome = null;
	private JRadioButton jRadioButtonPlay = null;
	private JRadioButton jRadioButtonManage = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JButton jButtonContinue = null;
	private JFrame framePlay = null;  //  @jve:decl-index=0:visual-constraint="29,23"
	private JPanel jPanel1 = null;
	private JPanel jPanel = null;
	private JPanel panelUpper = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JPanel jPanel11 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JLabel jLabel6 = null;
	private JLabel jLabel7 = null;
	/**
	 * This method initializes jRadioButtonPlay	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonPlay() {
		if (jRadioButtonPlay == null) {
			jRadioButtonPlay = new JRadioButton();
			jRadioButtonPlay.setBounds(new Rectangle(151, 213, 107, 25));
			jRadioButtonPlay.setFont(new Font("Dialog", Font.BOLD, 18));
			jRadioButtonPlay.setText("Play");
			jRadioButtonPlay.setSelected(true);
		}
		return jRadioButtonPlay;
	}

	/**
	 * This method initializes jRadioButtonManage	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonManage() {
		if (jRadioButtonManage == null) {
			jRadioButtonManage = new JRadioButton();
			jRadioButtonManage.setBounds(new Rectangle(273, 214, 111, 25));
			jRadioButtonManage.setFont(new Font("Dialog", Font.BOLD, 18));
			jRadioButtonManage.setText("Manage");
		}
		return jRadioButtonManage;
	}

	/**
	 * This method initializes jButtonContinue	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonContinue() {
		if (jButtonContinue == null) {
			jButtonContinue = new JButton();
			jButtonContinue.setBounds(new Rectangle(203, 250, 122, 29));
			jButtonContinue.setText("Continue");
			jButtonContinue.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (jRadioButtonPlay.isSelected()){
						setVisible(false);
						//getFramePlay().pack();
						getFramePlay().setVisible(true);
					}
						
				}
			});
		}
		return jButtonContinue;
	}

	/**
	 * This method initializes FramePlay	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	private JFrame getFramePlay() {
		if (framePlay == null) {
			framePlay = new JFrame();
			framePlay.setSize(new Dimension(931, 492));
			framePlay.setContentPane(getJPanel());
			framePlay.setTitle("Play");
		}
		return framePlay;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getPanelUpper(), BorderLayout.NORTH);
		}
		return jPanel;
	}

	/**
	 * This method initializes panelUpper	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelUpper() {
		if (panelUpper == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("JLabel");
			jLabel3.setAlignmentX(jLabel3.CENTER_ALIGNMENT);
			jLabel2 = new JLabel();
			jLabel2.setText("please choose 2 characters, then press \"Check\"");
			jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel2.setAlignmentX(jLabel2.CENTER_ALIGNMENT);
			panelUpper = new JPanel();
			panelUpper.setLayout(new BoxLayout(getPanelUpper(), BoxLayout.Y_AXIS));
			panelUpper.add(jLabel2, null);
			panelUpper.add(getJPanel11(), null);
		}
		return panelUpper;
	}

	/**
	 * This method initializes jPanel11	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			jLabel7 = new JLabel();
			jLabel7.setText("JLabel");
			//jLabel7.setAlignmentX(CENTER_ALIGNMENT);
			jLabel6 = new JLabel();
			jLabel6.setText("JLabel");
			jLabel5 = new JLabel();
			jLabel5.setText("Character II");
			jLabel4 = new JLabel();
			jLabel4.setText("");
			jLabel4.setAlignmentX(LEFT_ALIGNMENT);
			jLabel5.setAlignmentX(RIGHT_ALIGNMENT);
			
			jPanel11 = new JPanel();
			jPanel11.setLayout(new BorderLayout());
			jPanel11.add(jLabel6, BorderLayout.EAST);
			jPanel11.add(jLabel7, BorderLayout.WEST);
		}
		return jPanel11;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				main thisClass = new main();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public main() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(513, 323);
		this.setContentPane(getJContentPane());
		this.setTitle("DB Proj");
		this.setResizable(false);

		
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(69, 163, 367, 36));
			jLabel1.setFont(new Font("Dialog", Font.BOLD, 18));
			jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel1.setText("What would you like to do ?");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(69, 136, 362, 42));
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel.setFont(new Font("Dialog", Font.BOLD, 18));
			jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel.setText("welcome");
			jLabelWelcome = new JLabel();
			jLabelWelcome.setBounds(new Rectangle(68, 39, 367, 81));
			jLabelWelcome.setFont(new Font("Footlight MT Light", Font.BOLD, 72));
			jLabelWelcome.setVerticalAlignment(SwingConstants.CENTER);
			jLabelWelcome.setText("DB Project");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabelWelcome, null);
			jContentPane.add(getJRadioButtonPlay(), null);
			jContentPane.add(getJRadioButtonManage(), null);
			
			ButtonGroup bg = new ButtonGroup();
			bg.add(jRadioButtonManage);
			bg.add(jRadioButtonPlay);
			
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(getJButtonContinue(), null);
		}
		return jContentPane;
	}

}  //  @jve:decl-index=0:visual-constraint="960,22"
