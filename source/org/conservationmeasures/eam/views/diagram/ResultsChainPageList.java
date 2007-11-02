/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.project.Project;

public class ResultsChainPageList extends DiagramPageList
{
	public ResultsChainPageList(Project project)
	{
		super(project, new ObjectPoolTableModel(project, ObjectType.RESULTS_CHAIN_DIAGRAM, getTags()));
	}
	
	private static String[] getTags()
	{
		return new String[] {ResultsChainDiagram.TAG_LABEL};
	}

	public boolean isConceptualModelPageList()
	{
		return false;
	}

	public boolean isResultsChainPageList()
	{
		return true;
	}

	public int getManagedDiagramType()
	{
		return ObjectType.RESULTS_CHAIN_DIAGRAM;
	}
}
