package GUI.panels.Play.Tabs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import GUI.GuiHandler;
import GUI.buttons.IconButton;
import GUI.model.StatisticsModel;
import GUI.workers.StatisticsWorker;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import dataTypes.SearchResultObject;
import dataTypes.SuccessRateObject;

public class StatisticsTab extends JPanel{
	private static final long serialVersionUID = 1L;

	private int ARRAY_SIZE = 5;
	
	private JLabel topFiveSuccessfulFirstCharLabels[] = new JLabel[ARRAY_SIZE];
	private JLabel topFiveSuccessfulSecondCharLabels[] = new JLabel[ARRAY_SIZE];
	private JLabel topFiveSuccessfulDateLabels[] = new JLabel[ARRAY_SIZE];
	
	private JLabel topFivePopularFirstCharLabels[] = new JLabel[ARRAY_SIZE];
	private JLabel topFivePopularSecondCharLabels[] = new JLabel[ARRAY_SIZE];
	private JLabel topFivePopularCountLabels[] = new JLabel[ARRAY_SIZE];
	
	private JLabel successRateTotalLabel ;
	private JLabel successRateSucessLabel ;
	private JLabel successRateLabel ;
	
	private StatisticsModel model;
	
	public StatisticsTab(){
		super();
		
		buildTab();
	}
	
	private void buildTab(){
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		initializeLabels();
		
		createTopFiveLastSuccessfulSearchesPanel();
		add(Box.createRigidArea(new Dimension(0,15)));
		createTopFivePopularSearchesPanel();
		add(Box.createRigidArea(new Dimension(0,15)));
		createSuccessfulRatePanel();
		add(Box.createRigidArea(new Dimension(0,5)));
		
		IconButton refreshButton = new IconButton("refresh", "refresh.png");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateContent();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(refreshButton);
		add(buttonPanel);
		
		updateContent();
		
	}
	
	private void initializeLabels(){
		for (int i=0; i<ARRAY_SIZE; i++){
			topFiveSuccessfulFirstCharLabels[i] = new chartLabel(Font.PLAIN, 13);
			topFiveSuccessfulSecondCharLabels[i] = new chartLabel(Font.PLAIN, 13);
			topFiveSuccessfulDateLabels[i] = new chartLabel(Font.PLAIN, 13);
			
			topFivePopularFirstCharLabels[i] = new chartLabel(Font.PLAIN, 13);
			topFivePopularSecondCharLabels[i] = new chartLabel(Font.PLAIN, 13);
			topFivePopularCountLabels[i] = new chartLabel(Font.PLAIN, 13);
		}
		
		successRateTotalLabel = new chartLabel(Font.PLAIN, 13);
		successRateSucessLabel = new chartLabel(Font.PLAIN, 13);
		successRateLabel = new chartLabel(Font.PLAIN, 13);
	}
	
	private void clearLabels(){
		for (int i=0; i<ARRAY_SIZE; i++){
			topFiveSuccessfulFirstCharLabels[i].setText("");
			topFiveSuccessfulSecondCharLabels[i].setText("");
			topFiveSuccessfulDateLabels[i].setText("");
			
			topFivePopularFirstCharLabels[i].setText("");
			topFivePopularSecondCharLabels[i].setText("");
			topFivePopularCountLabels[i].setText("");
		}
		
		successRateTotalLabel.setText("");
		successRateSucessLabel.setText("");
		successRateLabel.setText("");
	}
	
	
	
	private void createTopFiveLastSuccessfulSearchesPanel(){
		createListPanel(topFiveSuccessfulFirstCharLabels, topFiveSuccessfulSecondCharLabels, topFiveSuccessfulDateLabels, "Date", "Top Last 5 Searches");
	}
	
	private void createTopFivePopularSearchesPanel(){
		createListPanel(topFivePopularFirstCharLabels, topFivePopularSecondCharLabels, topFivePopularCountLabels, "Count", "Top 5 Popular Searches");
	}
	
	private void createSuccessfulRatePanel() {
		FormLayout topFivelayout = new FormLayout("115dlu, center:pref, 10dlu, center:pref, 10dlu, center:pref", "p, 5dlu, p");
		PanelBuilder builder = new PanelBuilder(topFivelayout);

		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		JLabel header;
		
		header = new chartLabel(Font.BOLD, 15);
		header.setText("<html><u>Total</u></html>");
		builder.add(header, cc.xy(2, 1));
		builder.add(successRateTotalLabel, cc.xy(2,3));
		
		header = new chartLabel(Font.BOLD, 15);
		header.setText("<html><u>Success</u></html>");
		builder.add(header, cc.xy(4, 1));
		builder.add(successRateSucessLabel, cc.xy(4,3));
		
		header = new chartLabel(Font.BOLD, 15);
		header.setText("<html><u>Rate</u></html>");
		builder.add(header, cc.xy(6, 1));
		builder.add(successRateLabel, cc.xy(6,3));
		
		JPanel resultPanel = builder.getPanel();
		TitledBorder titleBorder = BorderFactory.createTitledBorder(null, "Success Rate", TitledBorder.CENTER,  TitledBorder.TOP, new Font("Footlight MT Light", Font.BOLD, 20));
		resultPanel.setBorder(titleBorder);
		resultPanel.setMaximumSize(new Dimension(500,100));
		
		add(resultPanel);
	}
	
	private void setHeaders(PanelBuilder builder, String extraHeader){
		CellConstraints cc = new CellConstraints();
		
		JLabel header ;
		
		header = new chartLabel(Font.BOLD, 15);
		header.setText("<html><u>1st. Character </u></html>");
		builder.add(header, cc.xy(3, 1));
		
		header = new chartLabel(Font.BOLD, 15);
		header.setText("<html><u>2nd. Character</u></html>");
		builder.add(header,cc.xy(5, 1));
		
		header = new chartLabel(Font.BOLD, 15);
		header.setText("<html><u>" + extraHeader + "</u></html>");
		builder.add(header, cc.xy(7, 1));
	}

	private void createListPanel(JComponent[] firstCharacters, JComponent[] secondCharacters, JComponent[] extraFields, String extraHeader, String title){
		FormLayout topFivelayout = new FormLayout("left:pref, 10dlu, pref, 10dlu, pref, 10dlu, pref", buildRowsSpecs(ARRAY_SIZE));
		PanelBuilder builder = new PanelBuilder(topFivelayout);

		builder.setDefaultDialogBorder();
	
		setHeaders(builder, extraHeader);
		
		CellConstraints cc = new CellConstraints();
		for (int i=0; i<ARRAY_SIZE; i++){
			JLabel label = new JLabel(Integer.toString(i+1) + ") ");
			label.setFont(new Font("Footlight MT Light", Font.BOLD, 15));
			
			builder.add(label, cc.xy(1, i*2+3));
			builder.add(firstCharacters[i], cc.xy(3, i*2+3));
			builder.add(secondCharacters[i], cc.xy(5, i*2+3));
			builder.add(extraFields[i], cc.xy(7, i*2+3));
		}
		
		JPanel resultPanel= builder.getPanel();
		TitledBorder titleBorder = BorderFactory.createTitledBorder(null, title, TitledBorder.CENTER,  TitledBorder.TOP, new Font("Footlight MT Light", Font.BOLD, 20));
		resultPanel.setBorder(titleBorder);
		resultPanel.setMaximumSize(new Dimension(700,200));
		
		add(resultPanel);
	}
	
	private String buildRowsSpecs(int nOfRows){
		int i;
		StringBuilder specs = new StringBuilder("");
		for (i=0 ; i<nOfRows ; i++ ){
			specs.append("p, 8dlu, ");
		}
		specs.append("p");
		return specs.toString();
	}
	
	private void updateContent(){
		StatisticsWorker worker = new StatisticsWorker(this);
		GuiHandler.startStatusFlash();
		worker.execute();
	}
	
	public void refreshFromModel(){
		clearLabels();
		SearchResultObject[] topPopularSearches = model.getTopPopularSearches();
		SearchResultObject[] topLastSearches = model.getLastSuccessSearches();
		SuccessRateObject successRate = model.getSucessRate();
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		for (int i=0; i<topLastSearches.length; i++){
			if (topLastSearches[i] != null){
				topFiveSuccessfulFirstCharLabels[i].setText(topLastSearches[i].getFirstCharacter());
				topFiveSuccessfulSecondCharLabels[i].setText(topLastSearches[i].getSecondCharacter());
				topFiveSuccessfulDateLabels[i].setText(format.format(topLastSearches[i].getDate()));
			}
		}
		
		for(int i=0; i<topPopularSearches.length; i++){
			if (topPopularSearches[i] != null){
			topFivePopularFirstCharLabels[i].setText(topPopularSearches[i].getFirstCharacter());
			topFivePopularSecondCharLabels[i].setText(topPopularSearches[i].getSecondCharacter());
			topFivePopularCountLabels[i].setText(Long.toString(topPopularSearches[i].getCount()));
			}
		}
			
		
		successRateTotalLabel.setText(Long.toString(successRate.getTotalSearches()));
		successRateSucessLabel.setText(Long.toString(successRate.getSuccessSearches()));
		if (successRate.getTotalSearches()!=0 ){
			successRateLabel.setText( Long.toString( ( successRate.getSuccessSearches()* 100) / successRate.getTotalSearches())   + "%");
		}
		else{
			successRateLabel.setText("0%");			
		}
	}
	
	public void setModel(StatisticsModel model){
		this.model = model;
	}
	
	public StatisticsModel getModel(){
		return model;
	}
	
	class chartLabel extends JLabel{
		private static final long serialVersionUID = 1L;

		public chartLabel(int fontStyle, int fontSize){
			super();
			setFont(new Font("Footlight MT Light", fontStyle, fontSize));
		}
	}
	
	
}
