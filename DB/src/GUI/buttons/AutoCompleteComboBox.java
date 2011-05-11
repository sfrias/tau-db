package GUI.buttons;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

public class AutoCompleteComboBox extends JComboBox	{
	private static final long serialVersionUID = 1L;
	
	public AutoCompleteComboBox(Object[] items) {
		super(items) ;
		setEditable(true);
		JTextComponent editor = (JTextComponent) getEditor().getEditorComponent();
		editor.setDocument(new S04FirstAutoCompletion(this));
		setSelectedItem(null);
	}
	
	public AutoCompleteComboBox() {
		super() ;
		setEditable(true);
		JTextComponent editor = (JTextComponent) getEditor().getEditorComponent();
		editor.setDocument(new S04FirstAutoCompletion(this));
		setSelectedItem(null);
	}
	
}
