/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;


public abstract class AbstractPlanningTreeDiagramNode extends AbstractPlanningTreeNode
{
	public AbstractPlanningTreeDiagramNode(Project projectToUse)
	{
		super(projectToUse);
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
