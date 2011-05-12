package GUI.panels.Manage.cards.add;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.commons.GuiUtils;
import GUI.commons.Pair;
import GUI.list.DisplayList;
import GUI.model.CharacterModel;
import GUI.workers.AddCharWorker;
import GUI.workers.GetCharacterRecordsWorker;

public class AddCharacters extends AddCard {
	private static final long serialVersionUID = 1L;
	ImageIcon addIcon = GuiUtils.readImageIcon("addIcon.png");
	ImageIcon okIcon  = GuiUtils.readImageIcon("okIcon.png");
	private AddCharacters me = this;
	private CharacterModel model;
	private DisplayList[] listIndex = new DisplayList[Tables.getMaxIndex()+1];
	                                                                   	
	private DisplayList creator;
	private DisplayList creatorValues;
	private JTextField addCreatorField = new JTextField(20);
	private DisplayList disease;
	private DisplayList diseaseValues;
	private JTextField addDiseaseField = new JTextField(20);
	private DisplayList ethnicity;
	private DisplayList ethnicityValues;
	private JTextField addEthnicityField = new JTextField(20);
	private DisplayList gender;
	private DisplayList genderValues;
	private JTextField addGenderField = new JTextField(20);
	private DisplayList job;
	private DisplayList jobValues;
	private JTextField addJobField = new JTextField(20);
	private DisplayList occupation;
	private DisplayList occupationValues;
	private JTextField addOccupationField = new JTextField(20);
	private DisplayList organization;
	private DisplayList organizationValues;
	private JTextField addOrganizationField = new JTextField(20);
	private DisplayList power;
	private DisplayList powerValues;
	private JTextField addPowerField = new JTextField(20);
	private DisplayList rank;
	private DisplayList rankValues;
	private JTextField addRankField = new JTextField(20);
	private DisplayList school;
	private DisplayList schoolValues;
	private JTextField addSchoolField = new JTextField(20);
	private DisplayList species;
	private DisplayList speciesValues;
	private JTextField addSpeciesField = new JTextField(20);
	private DisplayList universe;
	private DisplayList universeValues;
	private JTextField addUniverseField = new JTextField(20);
	
	Vector<String> titles = new Vector<String>();
	Vector<JComponent> components = new Vector<JComponent>();
	Vector<JComponent> extraAddPanels = new Vector<JComponent>();

	public AddCharacters(){
		super(Tables.characters, false);		
		populateVectors();
		populateLists();
		createListIndex();

		addFields(titles, components, extraAddPanels);
	}
	
	private void populateLists(){
		GetCharacterRecordsWorker worker = new GetCharacterRecordsWorker(this);
		GuiHandler.startStatusFlash();
		worker.execute();
	}
	
	private void populateVectors(){
		
		creatorValues = addEntries(Tables.creator, addCreatorField);
		diseaseValues = addEntries(Tables.disease, addDiseaseField);
		ethnicityValues = addEntries(Tables.ethnicity, addEthnicityField);
		genderValues =addEntries(Tables.gender, addGenderField);
		jobValues = addEntries(Tables.job, addJobField);
		occupationValues = addEntries(Tables.occupation, addOccupationField);
		organizationValues = addEntries(Tables.organization, addOrganizationField);
		powerValues = addEntries(Tables.power, addPowerField);
		rankValues = addEntries(Tables.rank, addRankField);
		schoolValues = addEntries(Tables.school, addSchoolField);
		speciesValues = addEntries(Tables.species, addSpeciesField);
		universeValues = addEntries(Tables.universe, addUniverseField);
	}
	
	private DisplayList addEntries(final Tables table, final JTextField addField){
		
		titles.add(table.toString().toLowerCase());
		
/*		Pair [] pairValues = createRecordList(table);
		values = new AutoCompleteComboBox(pairValues);*/
		DisplayList values = new DisplayList();
		JScrollPane sPane = new JScrollPane(values);
		sPane.setPreferredSize(new Dimension(150, 300));
		values.ensureIndexIsVisible(20);
		components.add(sPane);

		if (addField != null){
			final JButton button = new JButton(GuiUtils.readImageIcon("addIcon.png", 15, 15));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (addField.isVisible()){
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								button.setIcon(GuiUtils.readImageIcon("addIcon.png", 15, 15));
								String fieldName = table.toString()+"_name";
								String value = addField.getText();
								if (value.compareTo("")!=0){
									AddCharWorker worker = new AddCharWorker(table, fieldName, value,me);
									GuiHandler.startStatusFlash();								
									worker.execute();
								}
							}
						});
						addField.setVisible(false);
						revalidate();
					}
					else{
						addField.setVisible(true);
						button.setIcon(GuiUtils.readImageIcon("okIcon.png", 15, 15));
						revalidate();
					}
				}
			});
			
			JPanel panel = new JPanel();
			panel.add(addField);
			panel.add(button);
			addField.setVisible(false);
			extraAddPanels.add(panel);
		} else{
			extraAddPanels.add(null);
		}
		
		return values;
	}
	
/*	private Pair[][] getValues() {
		
		Pair[][] values = new Pair[14][];
		
		values[0] = new Pair[1];
		values[1] = (Pair []) creatorValues.getSelectedValue();
		values[2] = (Pair []) diseaseValues.getSelectedValue();
		values[3] = (Pair[]) ethnicityValues.getSelectedValue();
		values[4] = (Pair[]) genderValues.getSelectedValue();
		values[5] = (Pair[]) jobValues.getSelectedValue();
		values[7] = (Pair[]) occupationValues.getSelectedValue();
		values[8] = (Pair[]) organizationValues.getSelectedValue();
		values[9] = (Pair[]) powerValues.getSelectedValue();
		values[10] = (Pair[]) rankValues.getSelectedValue();
		values[11] = (Pair[]) schoolValues.getSelectedValue();
		values[12] = (Pair[]) speciesValues.getSelectedValue();
		values[13] = (Pair[]) universeValues.getSelectedValue();
		
		return values;
	}
	
	private String[] getFields() {
		
		String[] values = new String[14];
		
		values[0] = "character_name";
		values[1] = ((Pair) creator.getSelectedItem()).getName();
		values[2] = ((Pair) disease.getSelectedItem()).getName();
		values[3] = ((Pair) ethnicity.getSelectedItem()).getName();
		values[4] = ((Pair) gender.getSelectedItem()).getName();
		values[5] = ((Pair) job.getSelectedItem()).getName();
		values[6] = ((Pair) location.getSelectedItem()).getName();
		values[7] = ((Pair) occupation.getSelectedItem()).getName();
		values[8] = ((Pair) organization.getSelectedItem()).getName();
		values[9] = ((Pair) power.getSelectedItem()).getName();
		values[10] = ((Pair) rank.getSelectedItem()).getName();
		values[11] = ((Pair) school.getSelectedItem()).getName();
		values[12] = ((Pair) species.getSelectedItem()).getName();
		values[13] = ((Pair) universe.getSelectedItem()).getName();
		
		return values;

	}*/
	
	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						
						String fieldNames = table.toString()+"_name";
						String values = "";
						AddCharWorker worker = new AddCharWorker(table, fieldNames, values,me);
						GuiHandler.startStatusFlash();
						worker.execute();
					}
				});
			}
		};
	}

	@Override
	public void refreshFromModel() {
		for (int i=0; i<listIndex.length; i++){
			if (model.isAtrributeModified(i)){
				populateList(listIndex[i], model.getAttributePairs(i));
				// TODO listIndex[i].setSelectedIndex(listIndex[i].);
			}
		}
		
		if (GuiHandler.isStatusFlashing()){
			GuiHandler.stopStatusFlash();
		}
	}
	
	private void populateList(DisplayList list, Pair[] records){
		DefaultListModel model = new DefaultListModel();
		for (int i=0; i<records.length; i++){
			model.addElement(records[i]);
		}
		list.setModel(model);
	}
	
	public void setModel(CharacterModel model){
		this.model = model;
	}
	
	public CharacterModel getModel(){
		return model;
	}
	
	private void createListIndex(){
		listIndex[Tables.creator.getIndex()] = creatorValues;
		listIndex[Tables.disease.getIndex()] = diseaseValues;
		listIndex[Tables.ethnicity.getIndex()] = ethnicityValues;
		listIndex[Tables.gender.getIndex()] = genderValues;
		listIndex[Tables.job.getIndex()] = jobValues;
		listIndex[Tables.occupation.getIndex()] = occupationValues;
		listIndex[Tables.organization.getIndex()] = organizationValues;
		listIndex[Tables.power.getIndex()] = powerValues;
		listIndex[Tables.rank.getIndex()] = rankValues;
		listIndex[Tables.school.getIndex()] = schoolValues;
		listIndex[Tables.species.getIndex()] = speciesValues;
		listIndex[Tables.universe.getIndex()] = universeValues;
	}
	
}
