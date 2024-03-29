package GUI.panels.Play.Tabs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
import GUI.commons.GuiUtils;
import GUI.model.CharacterModel;
import GUI.model.SimpleModel;
import GUI.panels.Play.CharacterDisplayPanel;
import GUI.workers.AlgorithmWorker;
import GUI.workers.GetCharacterAttributesWorker;
import GUI.workers.GetRecordsByNameWorker;
import dataTypes.Character;
import dataTypes.Pair;



public class MainPlayTab extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private CharacterDisplayPanel panelRightDetails;
	private CharacterDisplayPanel panelLeftDetails;
	private AutoCompleteComboBox comboCharI = new AutoCompleteComboBox();
	private AutoCompleteComboBox comboCharII = new AutoCompleteComboBox();
	
	private SimpleModel simpleModel;
	

	private MainPlayTab me = this;	
	
	public MainPlayTab(){

		buildTab();
	}

	private void buildTab(){

		panelLeftDetails = new CharacterDisplayPanel();
		panelRightDetails = new CharacterDisplayPanel();

		JPanel panelLeftHead = createTopPanel(comboCharI, panelLeftDetails, "Character I", 1);
		JPanel panelRightHead = createTopPanel(comboCharII, panelRightDetails, "Character II", 2);

		JButton buttonCompare = GuiUtils.createActionButton("start", null, 
				new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					Pair pairI = (Pair)comboCharI.getSelectedItem();;
					Pair pairII = (Pair)comboCharII.getSelectedItem();			
					if (pairI == null || pairII == null){
						GuiHandler.showChooseCharactersDialog();
					} else {
						Character charI = panelLeftDetails.getSelectedCharacter();
						Character charII = panelRightDetails.getSelectedCharacter();
						AlgorithmWorker worker = new AlgorithmWorker(charI, charII, me);
						GuiHandler.startStatusFlash();
						worker.execute();
					}
				}catch (ClassCastException e){
					GuiHandler.showChooseFromComboDialog();
				}
			}
		});

		JPanel panelSelection = new JPanel();
		panelSelection.setLayout(new BoxLayout(panelSelection, BoxLayout.X_AXIS));
		panelSelection.add(panelLeftHead);
		panelSelection.add(Box.createHorizontalGlue());
		panelSelection.add(buttonCompare);
		panelSelection.add(Box.createHorizontalGlue());
		panelSelection.add(panelRightHead);

		JPanel panelDetails = new JPanel();
		panelDetails.setLayout(new BoxLayout(panelDetails, BoxLayout.X_AXIS));
		panelDetails.add(panelLeftDetails);
		panelDetails.add(panelRightDetails);

		JPanel panelSelectAndDetails = new JPanel(new BorderLayout());
		panelSelectAndDetails.add(panelSelection,BorderLayout.NORTH);
		panelSelectAndDetails.add(panelDetails,BorderLayout.CENTER);

		JLabel labeltTitle = new JLabel("Please choose two characters you would like to find a connection between");
		labeltTitle.setFont(new Font("Footlight MT Light", Font.BOLD, 20));
		labeltTitle.setAlignmentX(LEFT_ALIGNMENT);
		JPanel panelTitle = new JPanel();
		panelTitle.add(labeltTitle);

		JPanel panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		panelMain.add(panelTitle,BorderLayout.NORTH);
		panelMain.add(panelSelectAndDetails, BorderLayout.CENTER);

		panelMain.setBorder(new EmptyBorder(20,20,20,20));

		add(panelMain);
	}
	
	private JPanel createTopPanel(final AutoCompleteComboBox charComboBox, CharacterDisplayPanel charDetails, String characterTitle, final int charNum){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel labelChar = new JLabel(characterTitle);
		labelChar.setFont(new Font("Footlight MT Light", Font.PLAIN, 15));
		labelChar.setAlignmentX(CENTER_ALIGNMENT);
		charComboBox.setEditable(true);
		charComboBox.setPreferredSize(new Dimension(200, 20));
		charComboBox.addActionListener(new ComboActionListener(charNum, charDetails));

		JPanel panelCombo = new JPanel();
		panelCombo.add(charComboBox);

		JButton charSearchButton = new JButton("Search");
		charSearchButton.setAlignmentX(CENTER_ALIGNMENT);
		charSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Document doc = ((JTextComponent)charComboBox.getEditor().getEditorComponent()).getDocument();
				try {
					String queryString = doc.getText(0, doc.getLength());
					if (doc.getLength() == 0){
						GuiHandler.showNoEmptyStringDialog();
					} else if (!GuiUtils.isAscii(queryString)){
						GuiHandler.showOnlyAsciiDialog();
					} else {
						GetRecordsByNameWorker worker = new GetRecordsByNameWorker(me, charNum, queryString);
						GuiHandler.startStatusFlash();
						worker.execute();
					}
				} catch (BadLocationException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		panel.add(labelChar);
		panel.add(panelCombo);
		panel.add(charSearchButton);
		
		return panel;
	}
	
	private class ComboActionListener implements ActionListener {

		private int charNum;
		private CharacterDisplayPanel charDetails;

		public ComboActionListener(int charNum, CharacterDisplayPanel charDetails){
			super();
			this.charNum = charNum;
			this.charDetails = charDetails;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AutoCompleteComboBox cb = (AutoCompleteComboBox)e.getSource();
			Object selected = cb.getSelectedItem();
			if (selected != null && selected instanceof Pair){
				Pair record = (Pair) selected;
				if (record != null){
					charDetails.setCharDetails(record.getId(), record.getName());
					GetCharacterAttributesWorker worker = new GetCharacterAttributesWorker(me, record.getId(), charNum);
					GuiHandler.startStatusFlash();
					worker.execute();
				}
			}
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
	
	public void setSimpleModel(SimpleModel model){
		this.simpleModel = model;
	}

	public SimpleModel getSimpleModel(){
		return simpleModel;
	}
	
	
}

