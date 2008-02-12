/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.reports;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.project.ProjectChainObject;


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
