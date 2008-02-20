/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.Objective;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;


public abstract class AbstractPlanningTreeDiagramNode extends AbstractPlanningTreeNode
{
	public AbstractPlanningTreeDiagramNode(Project projectToUse, CodeList visibleRowsToUse)
	{
		super(projectToUse, visibleRowsToUse);
	}

	public void rebuild() throws Exception
	{
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor)project.findObject(diagramFactorRefs.get(i));
			if(diagramFactor.getWrappedType() != Target.getObjectType())
				continue;
			
			createAndAddChild(diagramFactor.getWrappedORef(), diagramObject);
		}
		
		Factor[] allWrappedFactors = diagramObject.getAllWrappedFactors();

		// NOTE: No need to search for Goals because they can only be inside Targets
		// NOTE: No need to search for Direct Threats because they can only be upstream of Targets
		addMissingChildren(extractThreatReductionResultRefs(allWrappedFactors), diagramObject);
		addMissingChildren(diagramObject.getAllObjectiveRefs(), diagramObject);
		addMissingChildren(extractNonDraftStrategyRefs(allWrappedFactors), diagramObject);
		addMissingChildren(extractIndicatorRefs(allWrappedFactors), diagramObject);
	}

	public BaseObject getObject()
	{
		return diagramObject;
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

	protected DiagramObject diagramObject;
}
