package GUI.panels.Manage.cards.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.ListModel;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.buttons.AutoCompleteComboBox;
import GUI.commons.Pair;
import GUI.list.DisplayList;
import GUI.model.CharacterModel;
import GUI.model.SimpleModel;
import GUI.panels.CharacterAttributePanel;
import GUI.workers.EditCharacterWorker;
import GUI.workers.GetAllAttributesWorker;
import GUI.workers.GetCharacterAttributesWorker;

public class EditCharacters extends EditCard{
	
	private static final long serialVersionUID = 1L;
	private EditCharacters me = this;
	private CharacterModel charModel;
	private SimpleModel simpleModel;
	private DisplayList[] allValuesIndex = new DisplayList[Tables.getMaxIndex()+1];
	private DisplayList[] characterValuesIndex = new DisplayList[Tables.getMaxIndex()+1];

	private DisplayList diseaseCharacterValues;
	private DisplayList diseaseValues;
	private DisplayList occupationCharacterValues;
	private DisplayList occupationValues;
	private DisplayList organizationCharacterValues;
	private DisplayList organizationValues;
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
		resetModel(powerCharacterValues);
		resetModel(schoolCharacterValues);
		resetModel(universeCharacterValues);

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
				Pair[][] values = getValues();
				EditCharacterWorker worker = new EditCharacterWorker(tables, values, me);
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

	private Pair[][] getValues() {

		Pair[][] values = new Pair[13][];

		values[0] = new Pair [] {new Pair(textName.getText(), -1)};
		values[2] = getPairs(diseaseCharacterValues);
		values[6] = getPairs(occupationCharacterValues);
		values[7] = getPairs(organizationCharacterValues);
		values[8] = getPairs(powerCharacterValues);
		values[10] = getPairs(schoolCharacterValues);
		values[12] = getPairs(universeCharacterValues);
		
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
		characterValuesIndex[Tables.power.getIndex()] = powerCharacterValues;
		characterValuesIndex[Tables.school.getIndex()] = schoolCharacterValues;
		characterValuesIndex[Tables.universe.getIndex()] = universeCharacterValues;
	}

	private void createAllAttributesListIndex(){
		
		allValuesIndex[Tables.disease.getIndex()] = diseaseValues;
		allValuesIndex[Tables.occupation.getIndex()] = occupationValues;
		allValuesIndex[Tables.organization.getIndex()] = organizationValues;
		allValuesIndex[Tables.power.getIndex()] = powerValues;
		allValuesIndex[Tables.school.getIndex()] = schoolValues;
		allValuesIndex[Tables.universe.getIndex()] = universeValues;
	}
}
