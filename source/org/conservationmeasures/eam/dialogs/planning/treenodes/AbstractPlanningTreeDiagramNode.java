package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;


public abstract class AbstractPlanningTreeDiagramNode extends AbstractPlanningTreeNode
{
	public AbstractPlanningTreeDiagramNode(Project projectToUse)
	{
		super(projectToUse);
	}

	protected boolean attemptToAddToPage(DiagramObject page, ORef refToAdd) throws Exception
	{
		boolean wasAdded = attemptToAddToChildren(refToAdd);
		if(wasAdded)
			return wasAdded;
		
		if(isGoalOnThisPage(page, refToAdd))
		{
			children.add(new PlanningTreeGoalNode(project, refToAdd));
			wasAdded = true;
		}
		
		if(isObjectiveOnThisPage(page, refToAdd))
		{
			children.add(new PlanningTreeObjectiveNode(project, refToAdd));
			wasAdded = true;
		}
		
		return wasAdded;		
	}

	boolean isGoalOnThisPage(DiagramObject page, ORef refToAdd)
	{
		if(refToAdd.getObjectType() != Goal.getObjectType())
			return false;
		
		return page.getAllGoalRefs().contains(refToAdd);
	}
	
	boolean isObjectiveOnThisPage(DiagramObject page, ORef refToAdd)
	{
		if(refToAdd.getObjectType() != Objective.getObjectType())
			return false;
		
		return page.getAllObjectiveRefs().contains(refToAdd);
	}
}
