package GUI.panels.Manage.cards.add;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.commons.GuiUtils;
import GUI.commons.Pair;
import GUI.list.DisplayList;
import GUI.model.CharacterModel;
import GUI.panels.CharacterAttributePanel;
import GUI.workers.AddCharacterWorker;
import GUI.workers.GetCharacterRecordsWorker;

public class AddCharacters extends AddCard {
	private static final long serialVersionUID = 1L;
	ImageIcon addIcon = GuiUtils.readImageIcon("addIcon.png");
	ImageIcon okIcon  = GuiUtils.readImageIcon("okIcon.png");
	private AddCharacters me = this;
	private CharacterModel model;
	private DisplayList[] allValuesIndex = new DisplayList[Tables.getMaxIndex()+1];
	                                                                   	
	private DisplayList creator;
	private DisplayList creatorValues;
	private DisplayList disease;
	private DisplayList diseaseValues;
	private DisplayList ethnicity;
	private DisplayList ethnicityValues;
	private DisplayList gender;
	private DisplayList genderValues;
	private DisplayList job;
	private DisplayList jobValues;
	private DisplayList occupation;
	private DisplayList occupationValues;
	private DisplayList organization;
	private DisplayList organizationValues;
	private DisplayList power;
	private DisplayList powerValues;
	private DisplayList rank;
	private DisplayList rankValues;
	private DisplayList school;
	private DisplayList schoolValues;
	private DisplayList species;
	private DisplayList speciesValues;
	private DisplayList universe;
	private DisplayList universeValues;
	
	Vector<String> titles = new Vector<String>();
	Vector<JComponent> components = new Vector<JComponent>();
	Vector<JComponent> extraAddPanels = new Vector<JComponent>();

	public AddCharacters(){
		super(Tables.characters, false);		
		populateVectors();
		populateLists();
		createListIndex();

		addFields(titles, components);
		
	}
	
	private void populateLists(){
		GetCharacterRecordsWorker worker = new GetCharacterRecordsWorker(this);
		GuiHandler.startStatusFlash();
		worker.execute();
	}
	
	private void populateVectors(){
		DisplayList [] lists;
		lists = addEntries(Tables.creator);
		creatorValues = lists[0];
		creator = lists[1];
		
		lists = addEntries(Tables.disease);
		diseaseValues = lists[0];
		disease = lists[1];
		
		lists = addEntries(Tables.ethnicity);
		ethnicityValues = lists[0];
		ethnicity = lists[1];
		
		lists = addEntries(Tables.gender);
		genderValues = lists[0];
		gender = lists[1];
		
		lists = addEntries(Tables.job);
		jobValues = lists[0];
		job = lists[1];
		
		lists = addEntries(Tables.occupation);
		occupationValues = lists[0];
		occupation = lists[1];
		
		lists = addEntries(Tables.organization);
		organizationValues = lists[0];
		organization = lists[1];
		
		lists = addEntries(Tables.power);
		powerValues = lists[0];
		power = lists[1];
		
		lists = addEntries(Tables.rank);
		rankValues = lists[0];
		rank = lists[1];

		lists = addEntries(Tables.school);
		schoolValues = lists[0];
		school = lists[1];
		
		lists = addEntries(Tables.species);
		speciesValues = lists[0];
		species = lists[1];	
		
		lists = addEntries(Tables.universe);
		universeValues = lists[0];
		universe = lists[1];
	}
	
	private DisplayList[] addEntries(final Tables table){
		
		titles.add(table.toString().toLowerCase());

		CharacterAttributePanel panel = new CharacterAttributePanel(table, this);
		
		components.add(panel);

		DisplayList allValuesList = panel.getAllValues();
		DisplayList selectedValuesList = panel.getSelectedValues();
		
		DisplayList[] lists = new DisplayList[]{allValuesList,selectedValuesList};
		return lists;

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
						AddCharacterWorker worker = new AddCharacterWorker(table, fieldNames, values,me);
						GuiHandler.startStatusFlash();
						worker.execute();
					}
				});
			}
		};
	}

	@Override
	public void refreshFromModel() {
		for (int i=0; i<allValuesIndex.length; i++){
			if (model.isAtrributeModified(i)){
				populateList(allValuesIndex[i], model.getAttributePairs(i));
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
		allValuesIndex[Tables.creator.getIndex()] = creatorValues;
		allValuesIndex[Tables.disease.getIndex()] = diseaseValues;
		allValuesIndex[Tables.ethnicity.getIndex()] = ethnicityValues;
		allValuesIndex[Tables.gender.getIndex()] = genderValues;
		allValuesIndex[Tables.job.getIndex()] = jobValues;
		allValuesIndex[Tables.occupation.getIndex()] = occupationValues;
		allValuesIndex[Tables.organization.getIndex()] = organizationValues;
		allValuesIndex[Tables.power.getIndex()] = powerValues;
		allValuesIndex[Tables.rank.getIndex()] = rankValues;
		allValuesIndex[Tables.school.getIndex()] = schoolValues;
		allValuesIndex[Tables.species.getIndex()] = speciesValues;
		allValuesIndex[Tables.universe.getIndex()] = universeValues;
	}
	
}
