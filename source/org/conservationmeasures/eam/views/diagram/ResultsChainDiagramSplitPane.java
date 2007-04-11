/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Component;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.main.MainWindow;

public class ResultsChainDiagramSplitPane extends DiagramSplitPane
{
	public ResultsChainDiagramSplitPane(MainWindow mainWindow, DiagramComponent diagramComponent)
	{
		super(mainWindow, diagramComponent);
	}

	public Component createLegendPanel(MainWindow mainWindow)
	{
		return new ResultsChainDiagramLegendPanel(mainWindow);
	}
}
