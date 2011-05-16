package GUI.panels.Manage.cards.add;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.ListModel;
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

	public void setModel(CharacterModel model){
		this.model = model;
	}

	public CharacterModel getModel(){
		return model;
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

	private Pair[][] getValues() {

		Pair[][] values = new Pair[13][];

		values[0] = new Pair [] {new Pair(textName.getText(), -1)};
		values[1] = getPairs(creator);
		values[2] = getPairs(disease);
		values[3] = getPairs(ethnicity);ethnicity.getSelectedValues();
		values[4] = getPairs(gender);gender.getSelectedValues();
		values[5] = getPairs(job);
		values[6] = getPairs(occupation);
		values[7] = getPairs(organization);
		values[8] = getPairs(power);
		values[9] = getPairs(rank);
		values[10] = getPairs(school);
		values[11] = getPairs(species);
		values[12] = getPairs(universe);
		return values;
	}

	private Pair[] getPairs(DisplayList list){
		
		ListModel model = list.getModel();
		
		Pair[] values = new Pair[model.getSize()];
		for (int i=0; i < model.getSize(); i++) {
			values[i] = (Pair) model.getElementAt(i);
		}
		
		return values;
	}

	private String[] getTablesNames() {

		String [] values = new String[13];
		values[0] = "characters";
		values[1] = Tables.creator.name();
		values[2] = Tables.disease.name();
		values[3] = Tables.ethnicity.name();
		values[4] = Tables.gender.name();
		values[5] = Tables.job.name();
		values[6] = Tables.occupation.name();
		values[7] = Tables.organization.name();
		values[8] = Tables.power.name();
		values[9] = Tables.rank.name();
		values[10] = Tables.school.name();
		values[11] = Tables.species.name();
		values[12] = Tables.universe.name();

		return values;
	}

	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						String[] tables = getTablesNames();
						Pair[][] values = getValues();
						AddCharacterWorker worker = new AddCharacterWorker(tables, values, me);
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
				//model.resetAttributeCell(i);
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
