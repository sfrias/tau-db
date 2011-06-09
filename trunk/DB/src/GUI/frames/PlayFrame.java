package GUI.frames;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import GUI.GuiHandler;
import GUI.commons.GuiUtils;
import GUI.panels.CustomGlassPane;
import GUI.panels.Play.Tabs.MainPlayTab;
import GUI.panels.Play.Tabs.StatisticsTab;

public class PlayFrame extends GenericFrame {
	private static final long serialVersionUID = 1L;

	public PlayFrame(){
		super();
		
		CustomGlassPane glass = new CustomGlassPane(null, getContentPane());
		GuiHandler.setGlassPane(glass);
		setGlassPane(glass);
		
		buildFrame();
	}

	private void buildFrame(){
		setTitle("Play");
		setSize(800,675);
		setResizable(false);
		add(BorderLayout.CENTER, tabPanelBuilder());

	}
	
	private JTabbedPane tabPanelBuilder(){
		JTabbedPane tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("PLAY", GuiUtils.readImageIcon("playTab.png"), new MainPlayTab());
		tabbedPanel.addTab("STATISTICS", GuiUtils.readImageIcon("statistic.png"), new StatisticsTab());
		
		return tabbedPanel;
	}
}
