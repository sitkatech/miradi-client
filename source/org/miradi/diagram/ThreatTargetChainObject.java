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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;

public class ThreatTargetChainObject
{
	public ThreatTargetChainObject(Project projectToUse)
	{
		project = projectToUse;
	}
	
	private void initializeChain(Factor startingFactorToUse)
	{
		setStartingFactor(startingFactorToUse);
		resultingThreats = new HashSet();
		resultingTargets = new HashSet();
		processedLinks = new HashSet();
	}
	
	public ORefSet getUpstreamThreatRefsFromTarget(Factor startingFactorToUse)
	{
		HashSet<Factor> upstreamThreats = getUpstreamThreatsFromTarget(startingFactorToUse);
		
		return new ORefSet(upstreamThreats.toArray(new Factor[0]));
	}
	
	public HashSet<Factor> getUpstreamThreatsFromTarget(Factor startingFactorToUse)
	{
		initializeChain(startingFactorToUse);
		buildUpstreamChain();
		return resultingThreats;
	}
	
	public HashSet<Factor> getDownstreamTargetsFromThreat(Factor startingFactorToUse)
	{
		initializeChain(startingFactorToUse);
		buildDownstreamChain();
		return resultingTargets;
	}
	
	private void buildChain(int direction)
	{
		HashSet<Factor> linkedFactors = new HashSet();
		HashSet<Factor> unprocessedFactors = new HashSet();
		linkedFactors.add(getStartingFactor());
		
		ORefList allFactorLinkRefs = getProject().getFactorLinkPool().getRefList();
		unprocessedFactors.addAll(getFactorsToProcess(direction, allFactorLinkRefs, getStartingFactor()));
		
		while(unprocessedFactors.size() > 0)
		{
			Factor factorToProcess = (Factor)unprocessedFactors.toArray()[0];
			if (!linkedFactors.contains(factorToProcess))
			{
				linkedFactors.add(factorToProcess);
				unprocessedFactors.addAll(getFactorsToProcess(direction, allFactorLinkRefs, factorToProcess));
			}
			unprocessedFactors.remove(factorToProcess);
		}
	}

	private HashSet<Factor> getFactorsToProcess(int direction, ORefList allFactorLinkRefs, Factor factorToProcess)
	{
		HashSet<Factor> unprocessedFactors = new HashSet();
		for(int i = 0; i < allFactorLinkRefs.size(); ++i)
		{
			FactorLink link = FactorLink.find(getProject(), allFactorLinkRefs.get(i));
			Factor thisFactorToProcess = processLink(factorToProcess, link, direction);
			if (thisFactorToProcess == null)
			{
				continue;
			}
			
			if (thisFactorToProcess.isDirectThreat())
			{
				resultingThreats.add(thisFactorToProcess);
				
				if(FactorLink.isFrom(direction))
					unprocessedFactors.add(thisFactorToProcess);
			}
			else if (thisFactorToProcess.isTarget())
			{
				resultingTargets.add(thisFactorToProcess);
				unprocessedFactors.add(thisFactorToProcess);
			}
			else
			{
				unprocessedFactors.add(thisFactorToProcess);
			}
		}
		
		return unprocessedFactors;
	}

	private Factor processLink(Factor thisFactor, FactorLink factorLink, int direction)
	{
		ORef factorRef = factorLink.getFactorRef(direction);
		ORef oppositeFactorRef = factorLink.getOppositeFactorRef(direction);
		if(factorRef.equals(thisFactor.getRef()))
		{
			processedLinks.add(factorLink);	
			return Factor.findFactor(getProject(), oppositeFactorRef);
		}
		
		if (factorLink.isBidirectional() && oppositeFactorRef.equals(thisFactor.getRef()))
		{
			processedLinks.add(factorLink);
			return Factor.findFactor(getProject(), factorRef);
		}
		
		return null;
	}
	
	private void  buildUpstreamChain()
	{
		buildChain(FactorLink.TO);
	}
		
	private void buildDownstreamChain()
	{
		buildChain(FactorLink.FROM);
	}
	
	private void setStartingFactor(Factor startingFactorToUse)
	{
		startingFactor = startingFactorToUse;
	}

	private Factor getStartingFactor()
	{
		return startingFactor;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private HashSet<Factor> resultingThreats;
	private HashSet<Factor> resultingTargets;
	private HashSet<FactorLink> processedLinks;
	private Factor startingFactor;
	private Project project;
}
