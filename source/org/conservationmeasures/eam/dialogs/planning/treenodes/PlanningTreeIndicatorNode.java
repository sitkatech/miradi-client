/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeIndicatorNode extends AbstractPlanningTreeNode
{
	public PlanningTreeIndicatorNode(Project projectToUse, ORef indicatorRef) throws Exception
	{
		super(projectToUse);
		indicator = (Indicator)project.findObject(indicatorRef);
		rebuild();
	}

	public void rebuild() throws Exception
	{
		ORefList methodRefs = indicator.getMethods();
		createAndAddChildren(methodRefs, null);
	}

	public BaseObject getObject()
	{
		return indicator;
	}

	boolean shouldSortChildren()
	{
		return false;
	}

	Indicator indicator;
}
