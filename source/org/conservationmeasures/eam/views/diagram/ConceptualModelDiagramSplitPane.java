/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.main.MainWindow;

public class ConceptualModelDiagramSplitPane extends DiagramSplitPane
{
	public ConceptualModelDiagramSplitPane(MainWindow mainWindow, DiagramComponent diagramComponentToAdd)
	{
		super(mainWindow, diagramComponentToAdd);
	}

	public DiagramLegendPanel createLegendPanel(MainWindow mainWindow)
	{
		return new ConceptualModelDiagramLegendPanel(mainWindow);
	}
}
