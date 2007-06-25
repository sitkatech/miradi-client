/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiScrollPane;

abstract public class DiagramSplitPane extends JSplitPane
{
	public DiagramSplitPane(MainWindow mainWindow, DiagramComponent diagramComponentToAdd)
	{
		legendPanel = createLegendPanel(mainWindow);
		scrollableLegendPanel = new JScrollPane(legendPanel);
		selectionPanel = createPageList(mainWindow.getProject());
		UiScrollPane scrollPane = createDiagramPanel(mainWindow.getProject(), diagramComponentToAdd);
		setLeftComponent(createLeftPanel());
		setRightComponent(scrollPane);
		setDividerLocation(scrollableLegendPanel.getPreferredSize().width + 50);
	}
	
	private UiScrollPane createDiagramPanel(Project project, DiagramComponent diagram)
	{
		UiScrollPane uiScrollPane = new UiScrollPane(diagram);
		uiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		uiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		uiScrollPane.getHorizontalScrollBar().setUnitIncrement(project.getGridSize());
		uiScrollPane.getVerticalScrollBar().setUnitIncrement(project.getGridSize());
		
		return uiScrollPane;
	}
	
	protected JSplitPane createLeftPanel()
	{
		JSplitPane leftSideSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		leftSideSplit.setTopComponent(selectionPanel);
		leftSideSplit.setBottomComponent(scrollableLegendPanel);
		
		return leftSideSplit;
	}
	
	public DiagramLegendPanel getLegendPanel()
	{
		return legendPanel;
	}

	abstract public JComponent createPageList(Project project);
	
	abstract public DiagramLegendPanel createLegendPanel(MainWindow mainWindow);
	
	protected DiagramLegendPanel legendPanel;
	private JComponent selectionPanel;
	private JScrollPane scrollableLegendPanel;
}
