/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Component;
import java.awt.Dimension;

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
		Component legendPanel = createLegendPanel(mainWindow);
		UiScrollPane scrollPane = createDiagramPanel(mainWindow.getProject(), diagramComponentToAdd);
		setRightComponent(scrollPane);
		setLeftComponent(legendPanel);
		setDividerLocation(legendPanel.getPreferredSize().width);
		
		//FIXME this should be be here
		Dimension dimension = new Dimension(900, 600);
		setMinimumSize(dimension);
		setPreferredSize(dimension);
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

	abstract public Component createLegendPanel(MainWindow mainWindow);
}
