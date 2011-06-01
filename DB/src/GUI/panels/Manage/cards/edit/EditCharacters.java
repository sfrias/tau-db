package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import dataTypes.Pair;

import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.list.DisplayList;
import GUI.model.CharacterModel;
import GUI.model.SimpleModel;
import GUI.panels.CharacterAttributePanel;
import GUI.workers.EditCharacterWorker;
import GUI.workers.GetAllAttributesWorker;
import GUI.workers.GetCharacterAttributesWorker;
import enums.Tables;

public class EditCharacters extends EditCard{
	
	private static final long serialVersionUID = 1L;
	private EditCharacters me = this;
	private CharacterModel charModel;
	private SimpleModel simpleModel;
	private DisplayList[] allValuesIndex = new DisplayList[Tables.getMaxIndex()+1];
	private DisplayList[] characterValuesIndex = new DisplayList[Tables.getMaxIndex()+1];
	private DisplayList[] originalCharacterValuesIndex = new DisplayList[Tables.getMaxIndex()+1];

	private DisplayList diseaseCharacterValues;
	private DisplayList diseaseValues;
	private DisplayList occupationCharacterValues;
	private DisplayList occupationValues;
	private DisplayList organizationCharacterValues;
	private DisplayList organizationValues;
	private DisplayList placeOfBirthCharacterValues;
	private DisplayList placeOfBirthValues;
	private DisplayList powerCharacterValues;
	private DisplayList powerValues;
	private DisplayList schoolCharacterValues;
	private DisplayList schoolValues;
	private DisplayList universeCharacterValues;
	private DisplayList universeValues;

	Vector<String> titles = new Vector<String>();
	Vector<JComponent> components = new Vector<JComponent>();
	Vector<JComponent> extraAddPanels = new Vector<JComponent>();

	public EditCharacters(){
		
		super(Tables.characters, false, false);		
		populateVectors();
		populateLists();
		createAllAttributesListIndex();
		createCharacterAttributesListIndex();
		createOriginalCharacterAttributesListIndex();
		addFields(titles, components);

	}

	public void setCharacterModel(CharacterModel model){
		this.charModel = model;
	}

	public CharacterModel getCharacterModel(){
		return charModel;
	}

	public void setSimpleModel(SimpleModel model){
		this.simpleModel = model;
	}

	public SimpleModel getSimpleModel(){
		return simpleModel;
	}

	public void refreshFromModel(){
		comboRecord.removeAllItems();
		Pair[] pairs = simpleModel.getRecords();
		for (int i=0; i<pairs.length; i++){
			comboRecord.addItem(pairs[i]);
		}
	}

	
	public void refreshCharaterAttributesFromCharacterModel() {
		for (int i=0; i < characterValuesIndex.length; i++){
			if (charModel.isAtrributeModified(i)){
				populateList(characterValuesIndex[i], charModel.getAttributePairs(i));
				populateList(originalCharacterValuesIndex[i], charModel.getAttributePairs(i));
			}
		}

		if (GuiHandler.isStatusFlashing()){
			GuiHandler.stopStatusFlash();
		}
	}
	
	public void refreshAllAttributesFromCharacterModel() {
		for (int i=0; i < allValuesIndex.length; i++){
			if (charModel.isAtrributeModified(i)){
				populateList(allValuesIndex[i], charModel.getAttributePairs(i));
			}
		}

		if (GuiHandler.isStatusFlashing()){
			GuiHandler.stopStatusFlash();
		}
	}
	
	public void clearValues() {

		textName.setText("");
		resetModel(diseaseCharacterValues);
		resetModel(occupationCharacterValues);
		resetModel(organizationCharacterValues);
		resetModel(placeOfBirthCharacterValues);
		resetModel(powerCharacterValues);
		resetModel(schoolCharacterValues);
		resetModel(universeCharacterValues);
		
		comboRecord.setSelectedItem(null);
		
		if (GuiHandler.isStatusFlashing()){
			GuiHandler.stopStatusFlash();
		}
	}
	
	public ActionListener createRecordComboListener() {

		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				AutoCompleteComboBox cb = (AutoCompleteComboBox)e.getSource();
				Object selected = cb.getSelectedItem();
				if (selected != null && selected instanceof Pair){
					Pair record = (Pair) selected;
					if (record != null){
						textName.setText(record.getName());
						GetCharacterAttributesWorker worker = new GetCharacterAttributesWorker(me, record.getId(), -1);
						GuiHandler.startStatusFlash();
						worker.execute();
					}
				}
			}
		};
	}
	
	public ActionListener createActionButtonListener() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				String[] tables = getTablesNames();
				Pair[][] addedValues = getAddedValues();
				Pair[][] removedValues = getRemovedValues();
				Pair characerPair = (Pair) comboRecord.getSelectedItem();
				characerPair.setName(textName.getText());
				DefaultListModel listModel = (DefaultListModel) placeOfBirthCharacterValues.getModel();
				Pair placeOfBirthPair = (Pair) listModel.get(0);
				int placeOfBirthId = placeOfBirthPair.getId();
				EditCharacterWorker worker = new EditCharacterWorker(tables, addedValues, removedValues, characerPair, placeOfBirthId, me);
				GuiHandler.startStatusFlash();
				worker.execute();

			}
		};
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
		diseaseCharacterValues = lists[1];

		lists = addEntries(Tables.occupation);
		occupationValues = lists[0];
		occupationCharacterValues = lists[1];

		lists = addEntries(Tables.organization);
		organizationValues = lists[0];
		organizationCharacterValues = lists[1];
		
		lists = addEntries(Tables.place_of_birth);
		placeOfBirthValues = lists[0];
		placeOfBirthCharacterValues = lists[1];
		placeOfBirthValues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		lists = addEntries(Tables.power);
		powerValues = lists[0];
		powerCharacterValues = lists[1];

		lists = addEntries(Tables.school);
		schoolValues = lists[0];
		schoolCharacterValues = lists[1];

		lists = addEntries(Tables.universe);
		universeValues = lists[0];
		universeCharacterValues = lists[1];
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

	private Pair[][] getAddedValues() {

		Pair[][] values = new Pair[6][];

		values[Tables.disease.getIndex()] = getAddedPairs(diseaseCharacterValues, originalCharacterValuesIndex[Tables.disease.getIndex()]);
		values[Tables.occupation.getIndex()] = getAddedPairs(occupationCharacterValues, originalCharacterValuesIndex[Tables.occupation.getIndex()]);
		values[Tables.organization.getIndex()] = getAddedPairs(organizationCharacterValues, originalCharacterValuesIndex[Tables.organization.getIndex()]);
		values[Tables.power.getIndex()] = getAddedPairs(powerCharacterValues, originalCharacterValuesIndex[Tables.power.getIndex()]);
		values[Tables.school.getIndex()] = getAddedPairs(schoolCharacterValues, originalCharacterValuesIndex[Tables.school.getIndex()]);
		values[Tables.universe.getIndex()] = getAddedPairs(universeCharacterValues, originalCharacterValuesIndex[Tables.universe.getIndex()]);
		
		return values;
	}
	
	private Pair[][] getRemovedValues() {

		Pair[][] values = new Pair[6][];

		values[Tables.disease.getIndex()] = getRemovedPairs(diseaseCharacterValues, originalCharacterValuesIndex[Tables.disease.getIndex()]);
		values[Tables.occupation.getIndex()] = getRemovedPairs(occupationCharacterValues, originalCharacterValuesIndex[Tables.occupation.getIndex()]);
		values[Tables.organization.getIndex()] = getRemovedPairs(organizationCharacterValues, originalCharacterValuesIndex[Tables.organization.getIndex()]);
		values[Tables.power.getIndex()] = getRemovedPairs(powerCharacterValues, originalCharacterValuesIndex[Tables.power.getIndex()]);
		values[Tables.school.getIndex()] = getRemovedPairs(schoolCharacterValues, originalCharacterValuesIndex[Tables.school.getIndex()]);
		values[Tables.universe.getIndex()] = getRemovedPairs(universeCharacterValues, originalCharacterValuesIndex[Tables.universe.getIndex()]);
		
		return values;
	}

	private Pair[] getAddedPairs(DisplayList list, DisplayList originalList){

		DefaultListModel model = (DefaultListModel) list.getModel();
		DefaultListModel originalModel = (DefaultListModel) originalList.getModel();

		List<Pair> values = new ArrayList<Pair>();
		for (int i=0; i < model.getSize(); i++) {
			Object currentPair = model.getElementAt(i);
			if (!originalModel.contains(currentPair)){
				values.add((Pair) currentPair);
			}
		}
		return values.toArray(new Pair[values.size()]);
	}
	
	private Pair[] getRemovedPairs(DisplayList list, DisplayList originalList){

		DefaultListModel model = (DefaultListModel) list.getModel();
		DefaultListModel originalModel = (DefaultListModel) originalList.getModel();

		List<Pair> values = new ArrayList<Pair>();
		for (int i=0; i < originalModel.getSize(); i++) {
			Object currentPair = originalModel.getElementAt(i);
			if (!model.contains(currentPair)){
				values.add((Pair) currentPair);
			}
		}
		return values.toArray(new Pair[values.size()]);
	}
	
	private String[] getTablesNames() {

		String [] values = new String[6];
		values[Tables.disease.getIndex()] = Tables.disease.name();
		values[Tables.occupation.getIndex()] = Tables.occupation.name();
		values[Tables.organization.getIndex()] = Tables.organization.name();
		values[Tables.power.getIndex()] = Tables.power.name();
		values[Tables.school.getIndex()] = Tables.school.name();
		values[Tables.universe.getIndex()] = Tables.universe.name();

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
	
	private void createCharacterAttributesListIndex(){
		
		characterValuesIndex[Tables.disease.getIndex()] = diseaseCharacterValues;
		characterValuesIndex[Tables.occupation.getIndex()] = occupationCharacterValues;
		characterValuesIndex[Tables.organization.getIndex()] = organizationCharacterValues;
		characterValuesIndex[Tables.place_of_birth.getIndex()] = placeOfBirthCharacterValues;
		characterValuesIndex[Tables.power.getIndex()] = powerCharacterValues;
		characterValuesIndex[Tables.school.getIndex()] = schoolCharacterValues;
		characterValuesIndex[Tables.universe.getIndex()] = universeCharacterValues;
	}

	private void createOriginalCharacterAttributesListIndex(){
		
		originalCharacterValuesIndex[Tables.disease.getIndex()] = new DisplayList();
		originalCharacterValuesIndex[Tables.occupation.getIndex()] = new DisplayList();
		originalCharacterValuesIndex[Tables.organization.getIndex()] = new DisplayList();
		originalCharacterValuesIndex[Tables.place_of_birth.getIndex()] = new DisplayList();
		originalCharacterValuesIndex[Tables.power.getIndex()] = new DisplayList();
		originalCharacterValuesIndex[Tables.school.getIndex()] = new DisplayList();
		originalCharacterValuesIndex[Tables.universe.getIndex()] = new DisplayList();
	}
	
	private void createAllAttributesListIndex(){
		
		allValuesIndex[Tables.disease.getIndex()] = diseaseValues;
		allValuesIndex[Tables.occupation.getIndex()] = occupationValues;
		allValuesIndex[Tables.organization.getIndex()] = organizationValues;
		allValuesIndex[Tables.place_of_birth.getIndex()] = placeOfBirthValues;
		allValuesIndex[Tables.power.getIndex()] = powerValues;
		allValuesIndex[Tables.school.getIndex()] = schoolValues;
		allValuesIndex[Tables.universe.getIndex()] = universeValues;
	}
}
