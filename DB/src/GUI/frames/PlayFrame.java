package GUI.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import GUI.GuiHandler;
import GUI.buttons.QueryComboBox;
import GUI.commons.Pair;
import GUI.model.SimpleModel;
import GUI.panels.Play.CharacterDisplayPanel;

public class PlayFrame extends GenericFrame {
	private static final long serialVersionUID = 1L;

	private QueryComboBox comboCharI ;
	private QueryComboBox comboCharII;
	private CharacterDisplayPanel panelRightDetails;
	private CharacterDisplayPanel panelLeftDetails;
	private SimpleModel simpleModel;
	
	public PlayFrame(){
		super();
		
		setGlassPane(GuiHandler.getGlassPane());
		buildFrame();
	}

	private void buildFrame(){
		setTitle("Play");
		setSize(800,650);
		add(BorderLayout.CENTER, mainPanelBuilder());

	}



	private JPanel mainPanelBuilder(){

		JPanel panelLeftHead = new JPanel();
		panelLeftHead.setLayout(new BoxLayout(panelLeftHead, BoxLayout.Y_AXIS));
		JLabel labelCharI = new JLabel("Character I");
		labelCharI.setAlignmentX(CENTER_ALIGNMENT);
		comboCharI = new QueryComboBox(this);
		comboCharI.setPreferredSize(new Dimension(200, 20));
		JPanel panelCombo1 = new JPanel();
		panelCombo1.add(comboCharI);
		panelLeftHead.add(labelCharI);
		panelLeftHead.add(panelCombo1);

		JPanel panelRightHead = new JPanel();
		panelRightHead.setLayout(new BoxLayout(panelRightHead,BoxLayout.Y_AXIS));
		JLabel labelCharII = new JLabel("Character II");
		labelCharII.setAlignmentX(CENTER_ALIGNMENT);
		comboCharII = new QueryComboBox(this);
		comboCharII.setPreferredSize(new Dimension(200, 20));
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

		panelLeftDetails = new CharacterDisplayPanel();
		panelRightDetails = new CharacterDisplayPanel();

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
	
	public void refreshFromModel(){
		comboCharI.setUpdating(true);
		comboCharI.removeAllItems();
		Pair[] pairs = simpleModel.getRecords();
		for (int i=0; i<pairs.length; i++){
			comboCharI.addItem(pairs[i]);
		}
		comboCharI.setUpdating(false);
		
	}
	
	public void setSimpleModel(SimpleModel model){
		this.simpleModel = model;
	}
	
	public SimpleModel getSimpleModel(){
		return simpleModel;
	}

}
