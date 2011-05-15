package GUI.buttons;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import Enums.Tables;
import GUI.GuiHandler;
import GUI.frames.PlayFrame;
import GUI.workers.GetRecordsByName;

public class QueryComboBox extends JComboBox{

	private final PlayFrame parent ;
	private boolean updating = false;

	
	
	public QueryComboBox(PlayFrame parent){
		super();

		this.parent = parent; 

		setEditable(true);

		JTextComponent editor = (JTextComponent)getEditor().getEditorComponent();
		editor.getDocument().addDocumentListener(new DocListener());
	}

	class DocListener implements DocumentListener{

		public void changedUpdate(DocumentEvent e) {}

		public void insertUpdate(DocumentEvent e) {
			System.out.println("added letters");
			if (!updating){
				generateRecords(e);
			}
		}

		public void removeUpdate(DocumentEvent e) {
			System.out.println("remove letters");
			if (!updating){
				generateRecords(e);
			}
			}
		

		private void generateRecords(DocumentEvent e){
			Document doc = e.getDocument();
			try {
				String queryString = doc.getText(0, doc.getLength());
				updating = true;
				GetRecordsByName worker = new GetRecordsByName(Tables.characters, parent, queryString);
				GuiHandler.startStatusFlash();
				worker.execute();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void setUpdating(boolean updateStatus){
		updating = updateStatus;
	}

}
