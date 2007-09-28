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
		for(int i = 0; i < methodRefs.size(); ++i)
			children.add(new PlanningTreeTaskNode(project, methodRefs.get(i)));
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
