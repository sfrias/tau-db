package GUI.panels.Manage.Tabs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


import GUI.GuiHandler;
import GUI.buttons.IconButton;
import GUI.commons.GuiUtils;
import GUI.panels.CustomGlassPane;
import GUI.workers.UpdateWorker;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import enums.UpdateResult;

public class UpdateTab extends JPanel implements PropertyChangeListener{
	private static final long serialVersionUID = 1L;

	private DescriptionPanel descDownload;
	private DescriptionPanel descExtract;
	private DescriptionPanel descUpdate;
	private DescriptionPanel descDelete;

	private UpdateWorker worker = null;
	private JButton cancelButton ;
	private IconButton startImportButton;
	private JProgressBar progressBar;
	
	private CustomGlassPane updateGlass;
	private CustomGlassPane generalGlass;
	
	private UpdateTab me = this;

	public UpdateTab(){
		super();

		buildTab();
		
		JComponent[] compArr = {cancelButton};
		updateGlass = new CustomGlassPane(compArr, this);
		generalGlass = new CustomGlassPane(null, GuiHandler.getCurrentFrame().getContentPane());
	}

	private void buildTab(){
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		startImportButton = new IconButton("start import", "import.png");
		startImportButton.setAlignmentX(CENTER_ALIGNMENT);
		startImportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				GuiHandler.setGlassPane(updateGlass);
				GuiHandler.getCurrentFrame().setGlassPane(updateGlass);
				startImportButton.setEnabled(false);
				worker = new UpdateWorker(me);
				worker.addPropertyChangeListener(me);
				worker.execute();
				cancelButton.setEnabled(true);

				progressBar.setString("downloading in progress");

			}

		});

		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		cancelButton.setFont(new Font("Footlight MT Light",Font.BOLD,15));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GuiHandler.setGlassPane(generalGlass);
				GuiHandler.getCurrentFrame().setGlassPane(generalGlass);
				if (worker != null){
					worker.cancel(true);
					worker = null;
				}	
			}
		})  ;
		cancelButton.setAlignmentX(CENTER_ALIGNMENT);

		JPanel progressPanel = createMainProgressPanel();
		progressPanel.setAlignmentX(CENTER_ALIGNMENT);

		progressBar = new JProgressBar(0, 100);
		progressBar.setMaximumSize(new Dimension(400,20));
		progressBar.setStringPainted(true);
		
		add(Box.createRigidArea(new Dimension(0, 25)));
		add(startImportButton);
		add(Box.createRigidArea(new Dimension(0, 15)));
		add(cancelButton);
		add(Box.createRigidArea(new Dimension(0, 35)));
		add(progressPanel);
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(progressBar);
		add(Box.createRigidArea(new Dimension(0, 15)));
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}

	private JPanel createMainProgressPanel(){
		descDownload = new DescriptionPanel("downloading dumps file");
		descExtract = new DescriptionPanel("extracting files");
		descUpdate = new DescriptionPanel("updating tables");
		descDelete = new DescriptionPanel("removing temporary files");

		FormLayout layout = new FormLayout("5dlu, left:pref","p, 4dlu, p, 4dlu, p, 4dlu, p");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.add(descDownload, cc.xy(2,1));
		builder.add(descExtract, cc.xy(2,3));
		builder.add(descUpdate, cc.xy(2,5));
		builder.add(descDelete, cc.xy(2,7));		

		JPanel panel = builder.getPanel();
		panel.setMaximumSize(new Dimension(370,200));

		return panel;
	}

	public void refreshTab(UpdateResult progress){
		switch (progress){
		case finish_download:
			descDownload.setEnabled(true);
			progressBar.setString("extracting in progress");
			break;
		case finish_extract:
			descExtract.setEnabled(true);
			break;
		case start_update_table:
			cancelButton.setEnabled(false);
			progressBar.setString("updating in progress");
			break;
		case finish_update_table:
			descUpdate.setEnabled(true);
			progressBar.setString("deleting in progress");
			break;
		case finish_delete:
			descDelete.setEnabled(true);
			break;
		case cancel_accepted:
			GuiHandler.showCancelOperationDialog();
			clearProgress();
			break;
		case done:
			progressBar.setString("update completed");
			GuiHandler.showUpdateCompleteDialog();
			clearProgress();
			break;
		case exception:
			GuiHandler.showResultExceptionDialog("update");
			clearProgress();
			break;
		}
	}

	private void clearProgress(){
		descDownload.setEnabled(false);
		descExtract.setEnabled(false);
		descUpdate.setEnabled(false);
		descDelete.setEnabled(false);
		cancelButton.setEnabled(true);
		progressBar.setValue(0);
		progressBar.setString(null);
		startImportButton.setEnabled(true);
		cancelButton.setEnabled(false);
		GuiHandler.stopStatusFlash();
	}



	class DescriptionPanel extends JPanel{
		private static final long serialVersionUID = 1L;

		private String description;
		private JLabel descriptionLabel;
		private JLabel statusIcon ;

		public DescriptionPanel(String description){
			super();

			this.description = description;
			descriptionLabel = new JLabel(description);
			descriptionLabel.setFont(new Font("Footlight MT Light",Font.BOLD,20));
			descriptionLabel.setEnabled(false);
			descriptionLabel.setAlignmentX(LEFT_ALIGNMENT);

			statusIcon = new JLabel(GuiUtils.readImageIcon("questionmark.png"));
			statusIcon.setAlignmentX(LEFT_ALIGNMENT);

			add(statusIcon);
			add(Box.createRigidArea(new Dimension(10,0)));
			add(descriptionLabel);
		}

		@Override
		public void setEnabled(boolean b){
			super.setEnabled(b);

			if (b){
				descriptionLabel.setEnabled(true);
				descriptionLabel.setText(description + "...Done!");
				statusIcon.setIcon(GuiUtils.readImageIcon("checked.png"));
			}else {
				descriptionLabel.setEnabled(false);
				descriptionLabel.setText(description);
				statusIcon.setIcon(GuiUtils.readImageIcon("questionmark.png"));
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("progress")) {
			if (worker != null){
				int progress = worker.getProgress();
				progressBar.setValue(progress);
			}
		} 

	}

}
