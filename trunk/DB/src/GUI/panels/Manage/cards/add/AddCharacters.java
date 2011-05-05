package GUI.panels.Manage.cards.add;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import GUI.workers.GetSimpleRecordsWorker;

public class AddCharacters extends AddCard {
	private static final long serialVersionUID = 1L;
	
	private AutoCompleteComboBox creator;
	private JTextField addCreatorField = new JTextField(50);
	private AutoCompleteComboBox disease;
	private JTextField addDiseaseField = new JTextField(50);
	private AutoCompleteComboBox ethnicity;
	private JTextField addEthnicityField = new JTextField(50);
	private AutoCompleteComboBox gender;
	private JTextField addGenderField = new JTextField(50);
	private AutoCompleteComboBox job;
	private JTextField addJobField = new JTextField(50);
	private AutoCompleteComboBox location;
	private JTextField addLocaionField = new JTextField(50);
	private AutoCompleteComboBox occupation;
	private JTextField addOccupationField = new JTextField(50);
	private AutoCompleteComboBox organization;
	private JTextField addOrganizationField = new JTextField(50);
	private AutoCompleteComboBox power;
	private JTextField addPowerField = new JTextField(50);
	private AutoCompleteComboBox rank;
	private JTextField addRankField = new JTextField(50);
	private AutoCompleteComboBox school;
	private JTextField addSchoolField = new JTextField(50);
	private AutoCompleteComboBox species;
	private JTextField addSpeciesField = new JTextField(50);
	private AutoCompleteComboBox universe;
	private JTextField addUniverseField = new JTextField(50);
	
	Vector<String> titles = new Vector<String>();
	Vector<JComponent> components = new Vector<JComponent>();
	Vector<JComponent> extraAddPanels = new Vector<JComponent>();

	public AddCharacters() throws Exception{
		super(Tables.characters, false);
		populateVectors();

		addFields(titles, components, extraAddPanels);
		switchCard(MAIN_CARD);
	}
	
	private void populateVectors() throws Exception{
		
		addEntries(Tables.creator, creator, addCreatorField);
		addEntries(Tables.disease, disease, addDiseaseField);
		addEntries(Tables.ethnicity, ethnicity, addEthnicityField);
		addEntries(Tables.gender, gender, addGenderField);
		addEntries(Tables.job, job, addJobField);
		addEntries(Tables.location, location, addLocaionField);
		addEntries(Tables.occupation, occupation, addOccupationField);
		addEntries(Tables.organization, organization, addOrganizationField);
		addEntries(Tables.power, power, addPowerField);
		addEntries(Tables.rank, rank, addRankField);
		addEntries(Tables.school, school, addSchoolField);
		addEntries(Tables.species, species, addSpeciesField);
		addEntries(Tables.universe, universe, addUniverseField);
	}
	
	private void addEntries(Tables table, AutoCompleteComboBox values, final JTextField addField) throws Exception{
		
		titles.add(table.toString().toLowerCase());
		
		Pair [] pairValues = createRecordList(table);
		values = new AutoCompleteComboBox(pairValues);
		values.setPreferredSize(new Dimension(200,20));

		components.add(values);
		if (addField != null){
			JButton button = new JButton("add");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					addField.setVisible(!addField.isVisible());
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
	}
	
	public Pair[] createRecordList(Tables table) throws Exception{
		
		GetSimpleRecordsWorker worker = new GetSimpleRecordsWorker(table);
		GuiHandler.startStatusFlash();
		worker.execute();
		try {
			Pair[] result = worker.get(2000,TimeUnit.MILLISECONDS);
			GuiHandler.stopStatusFlash();
			if (result != null){
				return result;
			} 
		} 
		catch (InterruptedException e) {}
		catch (ExecutionException e) {}
		catch (TimeoutException e) {}

		throw new Exception();
	}
	
}
