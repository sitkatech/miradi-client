/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.project.Project;

public class PlanningTreeConceptualModelPageNode extends AbstractPlanningTreeDiagramNode
{
	public PlanningTreeConceptualModelPageNode(Project projectToUse, ORef refToUse) throws Exception
	{
		super(projectToUse);
		diagramObject = (ConceptualModelDiagram)project.findObject(refToUse);
		rebuild();
	}

	public String getObjectTypeName()
	{
		return "Never Visible";
	}

}
