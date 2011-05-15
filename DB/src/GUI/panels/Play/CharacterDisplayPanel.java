package GUI.panels.Play;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Enums.Tables;
import GUI.commons.GuiUtils;
import GUI.list.DisplayList;
import GUI.model.CharacterModel;
import GUI.panels.CustomScrollPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class CharacterDisplayPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private DisplayList creator = new DisplayList();
	private DisplayList disease = new DisplayList();
	private DisplayList ethnicity = new DisplayList();
	private DisplayList gender = new DisplayList();
	private DisplayList rank = new DisplayList();
	private DisplayList job  = new DisplayList();
	private DisplayList occupation = new DisplayList();
	private DisplayList organization  = new DisplayList();
	private DisplayList power  = new DisplayList();
	private DisplayList school  = new DisplayList();
	private DisplayList species = new DisplayList();
	private DisplayList universe = new DisplayList();
	

	private CharacterModel charModel;
	private Vector<JComponent> listVector = new Vector<JComponent>();
	private Vector<String> namesVector = new Vector<String>();
	
	private  DisplayList[] listIndex = new DisplayList[Tables.getMaxIndex()+1];
	
	public static String CHAR_MODEL = "CHAR_MODEL";
	public static String SIMPLE_MODEL = "SIMPLE_MODEL";
	
	public CharacterDisplayPanel(){
		super();
		
		setLayout(new BorderLayout());
		populateLists();
		createListsIndex();
		addFields(namesVector, listVector);
	}
	
    public void addFields(Vector<String> fieldsNames, Vector<JComponent> fieldsComponents) {
		assert ((fieldsNames==null && fieldsComponents==null) ||
				(fieldsComponents.size()==fieldsNames.size()));
		
		int numOfRows = fieldsNames==null ? 0 : fieldsNames.size();
		FormLayout layout = new FormLayout("left:pref, 4dlu, pref", buildRowsSpecs(numOfRows));

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		for (int i=0; i<numOfRows; i++){
			builder.addLabel(fieldsNames.remove(0) + ":",cc.xy(1, 2*i+1));
			builder.add(fieldsComponents.remove(0),cc.xy(3,2*i+1));
		}
		
		JScrollPane panelScroll = new JScrollPane(builder.getPanel());
		panelScroll.setWheelScrollingEnabled(true);
		panelScroll.getVerticalScrollBar().setUnitIncrement(20);
		add(panelScroll,BorderLayout.CENTER);
	}
	
	
	private String buildRowsSpecs(int nOfRows){
		int i;
		StringBuilder specs = new StringBuilder("");
		for (i=0 ; i<nOfRows-1 ; i++ ){
			specs.append("p, 4dlu, ");
		}
		specs.append("p, 4dlu"); //one more for name field
		return specs.toString();
	}
	
	private void populateLists(){
		listVector.add(new CustomScrollPanel(creator));
		namesVector.add(Tables.creator.toString());
		listVector.add(new CustomScrollPanel(disease));
		namesVector.add(Tables.disease.toString());
		listVector.add(new CustomScrollPanel(ethnicity));
		namesVector.add(Tables.ethnicity.toString());
		listVector.add(new CustomScrollPanel(gender));
		namesVector.add(Tables.gender.toString());
		listVector.add(new CustomScrollPanel(job));
		namesVector.add(Tables.job.toString());
		listVector.add(new CustomScrollPanel(rank));
		namesVector.add(Tables.rank.toString());
		listVector.add(new CustomScrollPanel(occupation));
		namesVector.add(Tables.occupation.toString());
		listVector.add(new CustomScrollPanel(organization));
		namesVector.add(Tables.organization.toString());
		listVector.add(new CustomScrollPanel(power));
		namesVector.add(Tables.power.toString());
		listVector.add(new CustomScrollPanel(school));
		namesVector.add(Tables.school.toString());
		listVector.add(new CustomScrollPanel(species));
		namesVector.add(Tables.species.toString());
		listVector.add(new CustomScrollPanel(universe));
		namesVector.add(Tables.universe.toString());
	}
    

	
	public void setCharModel(CharacterModel model){
		this.charModel = model;
	}
	
	public CharacterModel getCharModel(){
		return charModel;
	}

	
	private void createListsIndex(){
		listIndex[Tables.creator.getIndex()] = creator;
		listIndex[Tables.disease.getIndex()] = disease;
		listIndex[Tables.ethnicity.getIndex()] = ethnicity;
		listIndex[Tables.gender.getIndex()] = gender;
		listIndex[Tables.job.getIndex()] = job;
		listIndex[Tables.occupation.getIndex()] = occupation;
		listIndex[Tables.organization.getIndex()] = organization;
		listIndex[Tables.power.getIndex()] = power;
		listIndex[Tables.rank.getIndex()] = rank;
		listIndex[Tables.school.getIndex()] = school;
		listIndex[Tables.species.getIndex()] = species;
		listIndex[Tables.universe.getIndex()] = universe;
		
	}
	
	public void refreshFromModel(){
		if (charModel != null){
			refreshFromCharModel();
		} else {
			//srefreshFromSimpleModel();
		}
	}
	
	private void refreshFromCharModel(){
		for (int i=0; i<listIndex.length; i++){
			if (charModel.isAtrributeModified(i)){
				GuiUtils.populateList(listIndex[i], charModel.getAttributePairs(i));
			}
		}
		charModel = null;
	}
	

	
	

}
