package GUI.panels.Manage.cards.add;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.GuiUtils;
import GUI.commons.Pair;
import GUI.model.CharacterModel;
import GUI.workers.AddCharWorker;
import GUI.workers.GetCharacterRecordsWorker;

public class AddCharacters extends AddCard {
	private static final long serialVersionUID = 1L;
	ImageIcon addIcon = GuiUtils.readImageIcon("addIcon.png");
	ImageIcon okIcon  = GuiUtils.readImageIcon("okIcon.png");
	private AddCharacters me = this;
	private CharacterModel model;
	private AutoCompleteComboBox[] comboIndex = new AutoCompleteComboBox[Tables.getMaxIndex()+1];
	                                                                   	
	private AutoCompleteComboBox creator;
	private JTextField addCreatorField = new JTextField(20);
	private AutoCompleteComboBox disease;
	private JTextField addDiseaseField = new JTextField(20);
	private AutoCompleteComboBox ethnicity;
	private JTextField addEthnicityField = new JTextField(20);
	private AutoCompleteComboBox gender;
	private JTextField addGenderField = new JTextField(20);
	private AutoCompleteComboBox job;
	private JTextField addJobField = new JTextField(20);
	private AutoCompleteComboBox location;
	private JTextField addLocaionField = new JTextField(20);
	private AutoCompleteComboBox occupation;
	private JTextField addOccupationField = new JTextField(20);
	private AutoCompleteComboBox organization;
	private JTextField addOrganizationField = new JTextField(20);
	private AutoCompleteComboBox power;
	private JTextField addPowerField = new JTextField(20);
	private AutoCompleteComboBox rank;
	private JTextField addRankField = new JTextField(20);
	private AutoCompleteComboBox school;
	private JTextField addSchoolField = new JTextField(20);
	private AutoCompleteComboBox species;
	private JTextField addSpeciesField = new JTextField(20);
	private AutoCompleteComboBox universe;
	private JTextField addUniverseField = new JTextField(20);
	
	Vector<String> titles = new Vector<String>();
	Vector<JComponent> components = new Vector<JComponent>();
	Vector<JComponent> extraAddPanels = new Vector<JComponent>();

	public AddCharacters(){
		super(Tables.characters, false);		
		populateVectors();
		
		populateCombos();
		createComboIndex();

		addFields(titles, components, extraAddPanels);
		//switchCard(MAIN_CARD);
	}
	
	private void populateCombos(){
		GetCharacterRecordsWorker worker = new GetCharacterRecordsWorker(this);
		GuiHandler.startStatusFlash();
		worker.execute();
	}
	
	private void populateVectors(){
		
		creator = addEntries(Tables.creator, creator, addCreatorField);
		disease = addEntries(Tables.disease, disease, addDiseaseField);
		ethnicity = addEntries(Tables.ethnicity, ethnicity, addEthnicityField);
		gender = addEntries(Tables.gender, gender, addGenderField);
		job = addEntries(Tables.job, job, addJobField);
		location = addEntries(Tables.location, location, addLocaionField);
		occupation = addEntries(Tables.occupation, occupation, addOccupationField);
		organization = addEntries(Tables.organization, organization, addOrganizationField);
		power = addEntries(Tables.power, power, addPowerField);
		rank = addEntries(Tables.rank, rank, addRankField);
		school = addEntries(Tables.school, school, addSchoolField);
		species = addEntries(Tables.species, species, addSpeciesField);
		universe = addEntries(Tables.universe, universe, addUniverseField);
	}
	
	private AutoCompleteComboBox addEntries(final Tables table, AutoCompleteComboBox values, final JTextField addField){
		
		titles.add(table.toString().toLowerCase());
		
/*		Pair [] pairValues = createRecordList(table);
		values = new AutoCompleteComboBox(pairValues);*/
		values = new AutoCompleteComboBox();
		values.setPreferredSize(new Dimension(200,20));
		components.add(values);

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
	
/*	public Pair[] createRecordList(Tables table) throws Exception{
		
		GetSimpleRecordsWorker worker = new GetSimpleRecordsWorker(table, this);
		GuiHandler.startStatusFlash();
		worker.execute();
		try {
			Pair[] result = worker.get(5000,TimeUnit.MILLISECONDS);
			GuiHandler.stopStatusFlash();
			if (result != null){
				return result;
			} 
		} 
		catch (InterruptedException e) {}
		catch (ExecutionException e) {}
		catch (TimeoutException e) {}

		throw new Exception();
	}*/
	
	private String[] getValues() {
		
		String[] values = new String[14];
		
		values[0] = textName.getText();
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

	}
	
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
		for (int i=0; i<comboIndex.length; i++){
			if (model.isAtrributeModified(i)){
				populateCombo(comboIndex[i], model.getAttributePairs(i));
				comboIndex[i].setSelectedItem(null);
			}
		}
		
		if (GuiHandler.isStatusFlashing()){
			GuiHandler.stopStatusFlash();
		}
	}
	
	private void populateCombo(AutoCompleteComboBox combo, Pair[] records){
		combo.removeAllItems();
		for (int i=0; i<records.length; i++){
			combo.addItem(records[i]);
		}
	}
	
	public void setModel(CharacterModel model){
		this.model = model;
	}
	
	public CharacterModel getModel(){
		return model;
	}
	
	private void createComboIndex(){
		comboIndex[Tables.creator.getIndex()] = creator;
		comboIndex[Tables.disease.getIndex()] = disease;
		comboIndex[Tables.ethnicity.getIndex()] = ethnicity;
		comboIndex[Tables.gender.getIndex()] = gender;
		comboIndex[Tables.job.getIndex()] = job;
		comboIndex[Tables.location.getIndex()] = location;
		comboIndex[Tables.occupation.getIndex()] = occupation;
		comboIndex[Tables.organization.getIndex()] = organization;
		comboIndex[Tables.power.getIndex()] = power;
		comboIndex[Tables.rank.getIndex()] = rank;
		comboIndex[Tables.school.getIndex()] = school;
		comboIndex[Tables.species.getIndex()] = species;
		comboIndex[Tables.universe.getIndex()] = universe;
	}
	
}
