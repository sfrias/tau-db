package GUI.buttons;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

public class AutoCompleteComboBox extends JComboBox	{
	public AutoCompleteComboBox(Object[] items) {
		super(items) ;
		setEditable(true);
		JTextComponent editor = (JTextComponent) getEditor().getEditorComponent();
		editor.setDocument(new S04FirstAutoCompletion(this));
	}
	
	public AutoCompleteComboBox() {
		super() ;
		setEditable(true);
		JTextComponent editor = (JTextComponent) getEditor().getEditorComponent();
		editor.setDocument(new S04FirstAutoCompletion(this));
	}
	
}