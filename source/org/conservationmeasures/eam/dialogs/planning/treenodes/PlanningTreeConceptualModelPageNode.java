/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.project.Project;

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
