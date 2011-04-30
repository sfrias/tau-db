package GUI.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.GuiUtils;

public class PlayFrame extends GenericFrame {
	private static final long serialVersionUID = 1L;
	
	JFrame frame = this;
	
	public PlayFrame(){
		super();
		buildFrame();
		//pack();
		GuiUtils.centerOnScreen(this);
		//TODO set the minimum size
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(GuiUtils.defaultCloseWindowAdapter(frame));
	}
	
	private void buildFrame(){
		setTitle("Play");
		setSize(600,600);
		add(BorderLayout.CENTER,mainPanelBuilder());
		setVisible(true);
		
	}
	
	private JPanel mainPanelBuilder(){
		
		JPanel panelLeftHead = new JPanel();
		panelLeftHead.setLayout(new BoxLayout(panelLeftHead, BoxLayout.Y_AXIS));
		JLabel labelCharI = new JLabel("Character I");
		labelCharI.setAlignmentX(CENTER_ALIGNMENT);
		AutoCompleteComboBox comboCharI = new AutoCompleteComboBox();
		comboCharI.setPreferredSize(new Dimension(150, 20));
		JPanel panelCombo1 = new JPanel();
		panelCombo1.add(comboCharI);
		panelLeftHead.add(labelCharI);
		panelLeftHead.add(panelCombo1);
		
		JPanel panelRightHead = new JPanel();
		panelRightHead.setLayout(new BoxLayout(panelRightHead,BoxLayout.Y_AXIS));
		JLabel labelCharII = new JLabel("Character II");
		labelCharII.setAlignmentX(CENTER_ALIGNMENT);
		AutoCompleteComboBox comboCharII = new AutoCompleteComboBox();
		comboCharII.setPreferredSize(new Dimension(150, 20));
		JPanel panelCombo2 = new JPanel();
		panelCombo2.add(comboCharII);
		panelRightHead.add(labelCharII);
		panelRightHead.add(panelCombo2);
		
		JButton buttonCompare = new JButton("Compare");
		buttonCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO enter logic
				
			}
		});
		
		JPanel panelSelection = new JPanel();
		panelSelection.setLayout(new BoxLayout(panelSelection, BoxLayout.X_AXIS));
		panelSelection.add(panelLeftHead);
		panelSelection.add(Box.createHorizontalGlue());
		panelSelection.add(buttonCompare);
		panelSelection.add(Box.createHorizontalGlue());
		panelSelection.add(panelRightHead);
		//panelSelection.setPreferredSize(new Dimension(10,0));
		
		JPanel panelLeftDetails = new JPanel();
		panelLeftDetails.setLayout(new BoxLayout(panelLeftDetails, BoxLayout.Y_AXIS));
		panelLeftDetails.setBorder(BorderFactory.createTitledBorder("Character I"));
		panelLeftDetails.setPreferredSize(new Dimension(500, 0));
		//panelLeftDetails.setMinimumSize(new Dimension(50, 100));
		panelLeftDetails.setAlignmentY(TOP_ALIGNMENT);
		
		JPanel panelRightDetails = new JPanel();
		panelRightDetails.setLayout(new BoxLayout(panelRightDetails, BoxLayout.Y_AXIS));
		panelRightDetails.setBorder(BorderFactory.createTitledBorder("Character II"));
		panelRightDetails.setPreferredSize(new Dimension(500, 0));
		//panelRightDetails.setMinimumSize(new Dimension(50, 100));
		panelRightDetails.setAlignmentY(TOP_ALIGNMENT);
		
		JPanel panelDetails = new JPanel();
		panelDetails.setLayout(new BoxLayout(panelDetails, BoxLayout.X_AXIS));
		panelDetails.add(panelLeftDetails);
		panelDetails.add(panelRightDetails);
		
		JPanel panelSelectAndDetails = new JPanel(new BorderLayout());
		panelSelectAndDetails.add(panelSelection,BorderLayout.NORTH);
		panelSelectAndDetails.add(panelDetails,BorderLayout.CENTER);
		
		JLabel labeltTitle = new JLabel("Please choose 2 characters:");
		labeltTitle.setAlignmentX(LEFT_ALIGNMENT);
		JPanel panelTitle = new JPanel();
		panelTitle.add(labeltTitle);
		panelTitle.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		panelMain.add(labeltTitle,BorderLayout.NORTH);
		panelMain.add(panelSelectAndDetails, BorderLayout.CENTER);
			
		panelMain.setBorder(new EmptyBorder(20,20,20,20));
		
		return panelMain;
	}

}
