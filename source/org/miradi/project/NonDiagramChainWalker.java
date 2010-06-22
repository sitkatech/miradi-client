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

import java.util.HashMap;
import java.util.Vector;

import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;


public class NonDiagramChainWalker
{	
	public NonDiagramChainWalker()
	{
		clearCaches();
	}

	public FactorSet buildNormalChainAndGetFactors(Factor factor)
	{
		buildNormalChain(factor);
		return getFactors();
	}
	public ORefSet buildNormalChainAndGetFactorRefs(Factor factor)
	{
		return buildNormalChainAndGetFactors(factor).getFactorRefs();
	}

	private FactorSet getFactors()
	{
		return factorSet;
	}

	private FactorSet getAllUpstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.TO);
	}
	
	private FactorSet getAllDownstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.FROM);
	}
	
	private Project getProject()
	{
		return startingFactor.getProject();
	}
	
	private void attemptToAdd(FactorLink thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}
	
	private FactorSet processLink(Factor thisFactor, FactorLink thisLink, int direction)
	{
		FactorSet newFactorIfAny = new FactorSet();
		if(thisLink.getFactorRef(direction).equals(thisFactor.getRef()))
		{
			attemptToAdd(thisLink);
			Factor linkedNode = (Factor) getProject().findObject(thisLink.getOppositeFactorRef(direction));
			newFactorIfAny.attemptToAdd(linkedNode);
			return newFactorIfAny;
		}
		
		if (!thisLink.isBidirectional())
			return newFactorIfAny;
		
		if(thisLink.getOppositeFactorRef(direction).equals(thisFactor.getRef()))
		{
			attemptToAdd(thisLink);
			Factor linkedNode = (Factor) getProject().findObject(thisLink.getFactorRef(direction));
			newFactorIfAny.attemptToAdd(linkedNode);
		}
		
		return newFactorIfAny;
	}
	
	public void clearCaches()
	{
		cachedUpstreamChain = new HashMap();
		cachedDownstreamChain = new HashMap();
	}

	private void buildNormalChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}

	private FactorSet getAllLinkedFactors(int direction)
	{
		HashMap<ORef, FactorSet> cache = getCache(direction);
		if(cache.containsKey(startingFactor.getRef()))
			return cache.get(startingFactor.getRef());
		FactorSet linkedFactors = new FactorSet();
		FactorSet unprocessedFactors = new FactorSet();
		linkedFactors.attemptToAdd(startingFactor);
		
		ORefList factorLinkRefs = getAllFactorLinkRefs();		
		unprocessedFactors.attemptToAddAll(getFactorsToProcess(direction, factorLinkRefs, startingFactor));
		
		while(unprocessedFactors.size() > 0)
		{
			Factor thisFactor = (Factor)unprocessedFactors.toArray()[0];
			if (!linkedFactors.contains(thisFactor))
			{
				linkedFactors.attemptToAdd(thisFactor);
				unprocessedFactors.attemptToAddAll(getFactorsToProcess(direction, factorLinkRefs, thisFactor));
			}
			unprocessedFactors.remove(thisFactor);
		}
		
		cache.put(startingFactor.getRef(), linkedFactors);
		return linkedFactors;
	}

	private FactorSet getFactorsToProcess(int direction, ORefList allFactorLinkRefs, Factor factorToProcess)
	{
		FactorSet unprocessedFactors = new FactorSet();
		for(int index = 0; index < allFactorLinkRefs.size(); ++index)
		{
			FactorLink factorLink = FactorLink.find(getProject(), allFactorLinkRefs.get(index));	
			unprocessedFactors.attemptToAddAll(processLink(factorToProcess, factorLink, direction));
		}
		
		return unprocessedFactors;
	}
	private HashMap<ORef, FactorSet> getCache(int direction)
	{
		switch(direction)
		{
			case FactorLink.FROM:
				return cachedDownstreamChain;
			case FactorLink.TO:
				return cachedUpstreamChain;
		}
		
		throw new RuntimeException("Unknown direction: " + direction);
	}
	private void initializeChain(Factor factor)
	{
		this.startingFactor = factor;
		factorSet = new FactorSet();
		processedLinks = new Vector();
	}
	private ORefList getAllFactorLinkRefs()
	{
		return getProject().getFactorLinkPool().getFactorLinkRefs();
	}

	private FactorSet factorSet;
	private Vector processedLinks;
	private Factor startingFactor;
	private HashMap<ORef, FactorSet> cachedUpstreamChain;
	private HashMap<ORef, FactorSet> cachedDownstreamChain;
}
