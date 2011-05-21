package GUI.panels.Manage.cards.add;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.commons.GuiUtils;
import GUI.commons.Pair;
import GUI.list.DisplayList;
import GUI.model.CharacterModel;
import GUI.panels.CharacterAttributePanel;
import GUI.workers.AddCharacterWorker;
import GUI.workers.GetAllAttributesWorker;

public class AddCharacters extends AddCard {
	private static final long serialVersionUID = 1L;
	ImageIcon addIcon = GuiUtils.readImageIcon("addIcon.png");
	ImageIcon okIcon  = GuiUtils.readImageIcon("okIcon.png");
	private AddCharacters me = this;
	private CharacterModel model;
	private DisplayList[] allValuesIndex = new DisplayList[Tables.getMaxIndex()+1];

	private DisplayList disease;
	private DisplayList diseaseValues;
	private DisplayList occupation;
	private DisplayList occupationValues;
	private DisplayList organization;
	private DisplayList organizationValues;
	private DisplayList placeOfBirth;
	private DisplayList placeOfBirthValues;
	private DisplayList power;
	private DisplayList powerValues;
	private DisplayList school;
	private DisplayList schoolValues;
	private DisplayList universe;
	private DisplayList universeValues;

	private Vector<String> titles = new Vector<String>();
	private Vector<JComponent> components = new Vector<JComponent>();

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

	
	public void clearValues() {
		
		textName.setText("");
		resetModel(disease);
		resetModel(occupation);
		resetModel(organization);
		resetModel(placeOfBirth);
		resetModel(power);
		resetModel(school);
		resetModel(universe);
		
		if (GuiHandler.isStatusFlashing()){
			GuiHandler.stopStatusFlash();
		}
	}
	
	private void populateLists(){
		GetAllAttributesWorker worker = new GetAllAttributesWorker(this);
		GuiHandler.startStatusFlash();
		worker.execute();
	}

	private void populateVectors(){
		DisplayList [] lists;

		lists = addEntries(Tables.disease);
		diseaseValues = lists[0];
		disease = lists[1];

		lists = addEntries(Tables.occupation);
		occupationValues = lists[0];
		occupation = lists[1];

		lists = addEntries(Tables.organization);
		organizationValues = lists[0];
		organization = lists[1];
		
		lists = addEntries(Tables.place_of_birth);
		placeOfBirthValues = lists[0];
		placeOfBirth = lists[1];
		placeOfBirthValues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		lists = addEntries(Tables.power);
		powerValues = lists[0];
		power = lists[1];
		
		lists = addEntries(Tables.school);
		schoolValues = lists[0];
		school = lists[1];
		
		lists = addEntries(Tables.universe);
		universeValues = lists[0];
		universe = lists[1];
	}

	private DisplayList[] addEntries(final Tables table){

		titles.add(table.toString());

		CharacterAttributePanel panel = new CharacterAttributePanel(table, this);

		components.add(panel);

		DisplayList allValuesList = panel.getAllValues();
		DisplayList selectedValuesList = panel.getSelectedValues();

		DisplayList[] lists = new DisplayList[]{allValuesList,selectedValuesList};
		return lists;

	}

	private Pair[][] getValues() {

		Pair[][] values = new Pair[Tables.getMaxIndex()+2][];

		values[0] = new Pair [] {new Pair(textName.getText(), -1)};
		values[Tables.disease.getIndex() + 1] = getPairs(disease);
		values[Tables.occupation.getIndex() + 1] = getPairs(occupation);
		values[Tables.organization.getIndex() + 1] = getPairs(organization);
		values[Tables.place_of_birth.getIndex() + 1] = getPairs(placeOfBirth);
		values[Tables.power.getIndex() + 1] = getPairs(power);
		values[Tables.school.getIndex() + 1] = getPairs(school);
		values[Tables.universe.getIndex() + 1] = getPairs(universe);
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

		String [] values = new String[Tables.getMaxIndex() + 2];
		values[0] = "characters";
		values[Tables.disease.getIndex() + 1] = Tables.disease.name();
		values[Tables.occupation.getIndex() + 1] = Tables.occupation.name();
		values[Tables.organization.getIndex() + 1] = Tables.organization.name();
		values[Tables.place_of_birth.getIndex() + 1] = Tables.place_of_birth.name();
		values[Tables.power.getIndex() + 1] = Tables.power.name();
		values[Tables.school.getIndex() + 1] = Tables.school.name();
		values[Tables.universe.getIndex() + 1] = Tables.universe.name();

		return values;
	}

	private void resetModel(DisplayList list){
		
		DefaultListModel model = new DefaultListModel();
		list.setModel(model);
	}
	
	private void populateList(DisplayList list, Pair[] records){
		DefaultListModel model = new DefaultListModel();
		for (int i=0; i<records.length; i++){
			model.addElement(records[i]);
		}
		list.setModel(model);
	}

	private void createListIndex(){
		allValuesIndex[Tables.disease.getIndex()] = diseaseValues;
		allValuesIndex[Tables.occupation.getIndex()] = occupationValues;
		allValuesIndex[Tables.organization.getIndex()] = organizationValues;
		allValuesIndex[Tables.place_of_birth.getIndex()] = placeOfBirthValues;
		allValuesIndex[Tables.power.getIndex()] = powerValues;
		allValuesIndex[Tables.school.getIndex()] = schoolValues;
		allValuesIndex[Tables.universe.getIndex()] = universeValues;
	}

}
