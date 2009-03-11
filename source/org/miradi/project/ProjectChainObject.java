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

public class ProjectChainObject  extends ChainObject
{
	public ProjectChainObject()
	{
		clearCaches();
	}

	void clearCaches()
	{
		cachedUpstreamChain = new HashMap();
		cachedDownstreamChain = new HashMap();
	}
	
	public FactorSet buildUpstreamChainAndGetFactors(Factor factor)
	{
		buildUpstreamChain(factor);
		return getFactors();
	}
	
	public FactorSet buildUpstreamDownstreamChainAndGetFactors(Factor factor)
	{
		buildUpstreamDownstreamChain(factor);
		return getFactors();
	}
	
	public FactorSet buildNormalChainAndGetFactors(Factor factor)
	{
		buildNormalChain(factor);
		return getFactors();
	}
	
	public ORefSet buildNormalChainAndGetFactorRefs(Factor factor)
	{
		buildNormalChain(factor);
		return getFactors().getFactorRefs();
	}
	
	private void buildDirectThreatChain(Factor factor)
	{
		initializeChain(factor);
		if(startingFactor.isDirectThreat())
		{
			factorSet.attemptToAddAll(getDirectlyLinkedDownstreamFactors());
			factorSet.attemptToAddAll(getAllUpstreamFactors());
		}
	}

	protected void buildNormalChain(Factor factor)
	{
		initializeChain(factor);
		if (startingFactor.isDirectThreat())
			buildDirectThreatChain(factor);
		else
			buildUpstreamDownstreamChain(factor);
	}
	
	protected void buildUpstreamDownstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	protected void buildUpstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	protected void buildDownstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
	}
	
	protected void buidDirectlyLinkedDownstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getDirectlyLinkedDownstreamFactors());
	}
	
	protected void buildDirectlyLinkedUpstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getDirectlyLinkedUpstreamFactors());
	}
	
	protected FactorSet getAllLinkedFactors(int direction)
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
	
	protected FactorSet getDirectlyLinkedFactors(int direction)
	{
		FactorSet results = new FactorSet();
		results.attemptToAdd(startingFactor);
		
		ORefList factorLinkRefs = getAllFactorLinkRefs();
		results.attemptToAddAll(getFactorsToProcess(direction, factorLinkRefs, startingFactor));

		return results;
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
	
	HashMap<ORef, FactorSet> cachedUpstreamChain;
	HashMap<ORef, FactorSet> cachedDownstreamChain;
}
