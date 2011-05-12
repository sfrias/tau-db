package GUI.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.commons.GuiUtils;
import GUI.list.DisplayList;
import GUI.panels.Manage.cards.add.AddCharacters;
import GUI.workers.AddCharacterAttributeWorker;

public class CharacterAttributePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DisplayList selectedValues;
	private DisplayList allValues;
	private JTextField addField = new JTextField(20);

	public CharacterAttributePanel(final Tables table, final AddCharacters addCharactersCard){
		super();

		setLayout(new BorderLayout());

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
									AddCharacterAttributeWorker worker = new AddCharacterAttributeWorker(table, fieldName, value, addCharactersCard);
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

			JPanel panelAddField = new JPanel();
			panelAddField.add(addField);
			panelAddField.add(button);
			addField.setVisible(false);

			JPanel panelBottom = new JPanel();
			panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.LINE_AXIS));
			panelBottom.add(panelAddField);
			add(panelBottom,BorderLayout.SOUTH);

			allValues = new DisplayList();
			CustomScrollPanel allValuesScrollPane = new CustomScrollPanel(allValues, 200, 80);

			final JButton addValueToListButton = new JButton(GuiUtils.readImageIcon("blueArrowLeft.png", 15, 15));
			addValueToListButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object[] values = selectedValues.getSelectedValues();
					DefaultListModel model = (DefaultListModel) selectedValues.getModel();
					for (int i=0; i<values.length; i++){
						model.removeElement(values[i]);
					}
					selectedValues.setModel(model);
					selectedValues.clearSelection();

				}
			});

			final JButton removeValueFromListButton = new JButton(GuiUtils.readImageIcon("blueArrowRight.png", 15, 15));
			removeValueFromListButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object[] values = allValues.getSelectedValues();
					DefaultListModel model = new DefaultListModel();
					for (int i=0; i<values.length; i++){
						model.addElement(values[i]);
					}
					selectedValues.setModel(model);
					allValues.clearSelection();
				}
			});

			JPanel panelAddRemoveValuesButtons = new JPanel();
			panelAddRemoveValuesButtons.setLayout(new BoxLayout(panelAddRemoveValuesButtons, BoxLayout.PAGE_AXIS));
			panelAddRemoveValuesButtons.add(removeValueFromListButton);
			panelAddRemoveValuesButtons.add(addValueToListButton);

			selectedValues = new DisplayList();
			CustomScrollPanel selectedValuesScrollPane = new CustomScrollPanel(selectedValues, 200, 80);

			JPanel panelCenter = new JPanel();
			panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.LINE_AXIS));
			panelCenter.add(allValuesScrollPane);
			panelCenter.add(Box.createRigidArea(new Dimension(5,0)));
			panelCenter.add(panelAddRemoveValuesButtons);
			panelCenter.add(Box.createRigidArea(new Dimension(5,0)));
			panelCenter.add(selectedValuesScrollPane);

			add(panelCenter,BorderLayout.CENTER);
		}
	}

	public DisplayList getSelectedValues() {
		return selectedValues;
	}

	public DisplayList getAllValues() {
		return allValues;
	}

}
