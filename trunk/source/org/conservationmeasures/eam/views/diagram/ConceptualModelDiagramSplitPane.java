/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class ConceptualModelDiagramSplitPane extends DiagramSplitPane
{
	public ConceptualModelDiagramSplitPane(MainWindow mainWindow) throws Exception
	{
		super(mainWindow, ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
	}

	public DiagramLegendPanel createLegendPanel(MainWindow mainWindow)
	{
		return new ConceptualModelDiagramLegendPanel(mainWindow);
	}
	
	public DiagramPageList createPageList(Project project)
	{
		return new ConceptualModelPageList(project);
	}
}
