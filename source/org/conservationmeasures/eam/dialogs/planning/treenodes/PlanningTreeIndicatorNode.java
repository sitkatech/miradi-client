package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeIndicatorNode extends AbstractPlanningTreeNode
{
	public PlanningTreeIndicatorNode(Project projectToUse, ORef indicatorRef)
	{
		super(projectToUse);
		indicator = (Indicator)project.findObject(indicatorRef);
	}

	public boolean attemptToAdd(ORef refToAdd) throws Exception
	{
		if(attemptToAddToChildren(refToAdd))
			return true;
		
		if(indicator.getMethods().contains(refToAdd))
		{
			children.add(new PlanningTreeTaskNode(project, refToAdd));
			return true;
		}

		return false;
	}

	public BaseObject getObject()
	{
		return indicator;
	}

	Indicator indicator;
}
