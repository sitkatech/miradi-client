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
package org.miradi.diagram;

import java.util.HashSet;

import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.Project;

public class ThreatTargetChainWalker
{
	public ThreatTargetChainWalker(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public ORefSet getUpstreamThreatRefsFromTarget(Factor startingFactorToUse)
	{
		ChainWalker walker = new ChainWalker();
		FactorSet upstreamFactors = walker.buildUpstreamChainAndGetFactors(startingFactorToUse);
		FactorSet upstreamThreats = extractThreatsOnly(upstreamFactors);
		
		return new ORefSet(upstreamThreats.toArray(new Factor[0]));
	}
	
	public HashSet<Cause> getUpstreamThreatsFromTarget(Factor startingFactorToUse)
	{
		ORefSet threatRefs = getUpstreamThreatRefsFromTarget(startingFactorToUse);
		HashSet<Cause> threats = new HashSet<Cause>();
		for(ORef threatRef : threatRefs)
		{
			threats.add(Cause.find(getProject(), threatRef));
		}
		
		return threats;
	}
	
	public ORefSet getDownstreamTargetRefsFromThreat(Factor startingFactorToUse)
	{
		ChainWalker walker = new ChainWalker();
		FactorSet upstreamFactors = walker.buildDownstreamChainAndGetFactors(startingFactorToUse);
		FactorSet targets = extractTargetsOnly(upstreamFactors);
		
		return new ORefSet(targets.toArray(new Factor[0]));
	}

	public HashSet<Factor> getDownstreamTargetsFromThreat(Factor startingFactorToUse)
	{
		ORefSet targetRefs = getDownstreamTargetRefsFromThreat(startingFactorToUse);
		HashSet<Factor> targets = new HashSet<Factor>();
		for(ORef targetRef : targetRefs)
		{
			targets.add(Target.findFactor(getProject().getObjectManager(), targetRef));
		}
		
		return targets;
	}
	
	private FactorSet extractThreatsOnly(FactorSet upstreamFactors)
	{
		FactorSet threats = new FactorSet();
		for(Factor factor : upstreamFactors)
		{
			if (factor.isDirectThreat())
				threats.attemptToAdd(factor);
		}
		
		return threats;
	}
	
	private FactorSet extractTargetsOnly(FactorSet upstreamFactors)
	{
		FactorSet threats = new FactorSet();
		for(Factor factor : upstreamFactors)
		{
			if (factor.isTarget())
				threats.attemptToAdd(factor);
		}
		
		return threats;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
