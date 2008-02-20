/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeResultsChainNode extends AbstractPlanningTreeDiagramNode
{
	public PlanningTreeResultsChainNode(Project projectToUse, ORef refToUse, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		diagramObject = (ResultsChainDiagram)project.findObject(refToUse);
		rebuild();
	}
}
