/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;


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
