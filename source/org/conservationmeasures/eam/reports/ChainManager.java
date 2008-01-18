/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectChainObject;


public class ChainManager
{
	public ChainManager(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public FactorSet findAllFactorsRelatedToThisIndicator(BaseId indicatorId) throws Exception
	{
		ORef ref = new ORef(ObjectType.INDICATOR, indicatorId);
		return findAllFactorsRelatedToThisObject(ref);
	}

	public FactorSet findAllFactorsRelatedToThisObjective(BaseId indicatorId) throws Exception
	{
		ORef ref = new ORef(ObjectType.OBJECTIVE, indicatorId);
		return findAllFactorsRelatedToThisObject(ref);
	}

	public FactorSet findAllFactorsRelatedToThisGoal(BaseId indicatorId) throws Exception
	{
		ORef ref = new ORef(ObjectType.GOAL, indicatorId);
		return findAllFactorsRelatedToThisObject(ref);
	}

	public FactorSet findAllFactorsRelatedToThisObject(ORef ref) throws Exception
	{
		BaseObject owner = getProject().findObject(ref);
		Factor owningFactor = owner.getDirectOrIndirectOwningFactor();
		FactorSet relatedFactors = new FactorSet();
		
		if(owningFactor != null)
		{
			ProjectChainObject chainObject = owner.getProjectChainBuilder();
			FactorSet nodesInChain = chainObject.buildUpstreamDownstreamChainAndGetFactors(owningFactor);
			
			relatedFactors.attemptToAddAll(nodesInChain);
		}
		
		return relatedFactors;
	}
	
	Project getProject()
	{
		return project;
	}
	
	Project project;
}
