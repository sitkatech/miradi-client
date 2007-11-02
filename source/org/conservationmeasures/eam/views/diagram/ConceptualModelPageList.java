/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.diagram.ConceptualModelPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.project.Project;


public class ConceptualModelPageList extends DiagramPageList
{
	public ConceptualModelPageList(Project project)
	{
		super(project, new ConceptualModelPoolTableModel(project, ObjectType.CONCEPTUAL_MODEL_DIAGRAM, getTags()));
	}

	private static String[] getTags()
	{
		return new String[] {ConceptualModelDiagram.TAG_LABEL};
	}
	
	public boolean isConceptualModelPageList()
	{
		return true;
	}

	public boolean isResultsChainPageList()
	{
		return false;
	}

	public int getManagedDiagramType()
	{
		return ObjectType.CONCEPTUAL_MODEL_DIAGRAM;
	}
}
