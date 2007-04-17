/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Component;

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
		setLeftComponent(legendPanel);
		setRightComponent(scrollPane);
		setDividerLocation(legendPanel.getPreferredSize().width);
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
