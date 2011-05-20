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
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import GUI.model.CharacterModel;
import GUI.model.SimpleModel;
import GUI.panels.Play.CharacterDisplayPanel;
import GUI.workers.GetCharacterAttributesWorker;
import GUI.workers.GetRecordsByNameWorker;

public class PlayFrame extends GenericFrame {
	private static final long serialVersionUID = 1L;

	private AutoCompleteComboBox comboCharI ;
	private AutoCompleteComboBox comboCharII;
	private CharacterDisplayPanel panelRightDetails;
	private CharacterDisplayPanel panelLeftDetails;
	private SimpleModel simpleModel;

	private PlayFrame playFrame = this;

	public PlayFrame(){
		super();

		//setGlassPane(GuiHandler.getGlassPane());
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
		comboCharI = new AutoCompleteComboBox();
		comboCharI.setEditable(true);
		comboCharI.setPreferredSize(new Dimension(200, 20));
		comboCharI.addActionListener(new ComboActionListener(1));

		JPanel panelCombo1 = new JPanel();
		panelCombo1.add(comboCharI);

		JButton charISearchButton = new JButton("Search");
		charISearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Document doc = ((JTextComponent)comboCharI.getEditor().getEditorComponent()).getDocument();
				String queryString;
				try {
					queryString = doc.getText(0, doc.getLength());
					GetRecordsByNameWorker worker = new GetRecordsByNameWorker(playFrame, 1, queryString);
					GuiHandler.startStatusFlash();
					worker.execute();
				} catch (BadLocationException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		panelLeftHead.add(labelCharI);
		panelLeftHead.add(panelCombo1);
		panelLeftHead.add(charISearchButton);

		JPanel panelRightHead = new JPanel();
		panelRightHead.setLayout(new BoxLayout(panelRightHead,BoxLayout.Y_AXIS));
		JLabel labelCharII = new JLabel("Character II");
		labelCharII.setAlignmentX(CENTER_ALIGNMENT);
		comboCharII = new AutoCompleteComboBox();
		comboCharII.setEditable(true);
		comboCharII.setPreferredSize(new Dimension(200, 20));
		comboCharII.addActionListener(new ComboActionListener(2));

		JPanel panelCombo2 = new JPanel();
		panelCombo2.add(comboCharII);

		JButton charIISearchButton = new JButton("Search");
		charIISearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Document doc = ((JTextComponent)comboCharII.getEditor().getEditorComponent()).getDocument();
				String queryString;
				try {
					queryString = doc.getText(0, doc.getLength());
					GetRecordsByNameWorker worker = new GetRecordsByNameWorker(playFrame, 2, queryString);
					GuiHandler.startStatusFlash();
					worker.execute();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		panelRightHead.add(labelCharII);
		panelRightHead.add(panelCombo2);
		panelRightHead.add(charIISearchButton);

		JButton buttonCompare = new JButton("Compare");
		buttonCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

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

		JLabel labeltTitle = new JLabel("Please choose two characters you would like to find a connection between");
		labeltTitle.setAlignmentX(LEFT_ALIGNMENT);
		JPanel panelTitle = new JPanel();
		panelTitle.add(labeltTitle);

		JPanel panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		panelMain.add(panelTitle,BorderLayout.NORTH);
		panelMain.add(panelSelectAndDetails, BorderLayout.CENTER);

		panelMain.setBorder(new EmptyBorder(20,20,20,20));

		return panelMain;
	}

	public void refreshCharIFromModel(){
		comboCharI.removeAllItems();
		Pair[] pairs = simpleModel.getRecords();
		for (int i=0; i<pairs.length; i++){
			comboCharI.addItem(pairs[i]);
		}
	}

	public void refreshCharIIFromModel(){
		comboCharII.removeAllItems();
		Pair[] pairs = simpleModel.getRecords();
		for (int i=0; i<pairs.length; i++){
			comboCharII.addItem(pairs[i]);
		}
	}	

	public void refreshPanelLeftDetailsFromModel(CharacterModel model){
		panelLeftDetails.setCharModel(model);
		panelLeftDetails.refreshFromModel();
	}

	public void refresPanelRightDetailsFromModel(CharacterModel model){
		panelRightDetails.setCharModel(model);
		panelRightDetails.refreshFromModel();
	}

	public void setSimpleModel(SimpleModel model){
		this.simpleModel = model;
	}

	public SimpleModel getSimpleModel(){
		return simpleModel;
	}

	private class ComboActionListener implements ActionListener {

		private int charNum;

		public ComboActionListener(int charNum){
			super();
			this.charNum = charNum;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AutoCompleteComboBox cb = (AutoCompleteComboBox)e.getSource();
			Object selected = cb.getSelectedItem();
			if (selected != null && selected instanceof Pair){
				Pair record = (Pair) selected;
				if (record != null){
					GetCharacterAttributesWorker worker = new GetCharacterAttributesWorker(playFrame, record.getId(), charNum);
					GuiHandler.startStatusFlash();
					worker.execute();
				}
			}
		}
	}
}
