/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.project;

import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;

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
		FactorLinkPool factorLinkPool = getProject().getFactorLinkPool();
		
		FactorLinkId[] linkIds = factorLinkPool.getFactorLinkIds();
		for(int i = 0; i < linkIds.length; ++i)
		{
			FactorLink thisLink = factorLinkPool.find(linkIds[i]);
			processLink(unprocessedFactors, startingFactor, thisLink, direction);
		}		
		
		while(unprocessedFactors.size() > 0)
		{
			Factor thisFactor = (Factor)unprocessedFactors.toArray()[0];
			if (!linkedFactors.contains(thisFactor))
			{
				linkedFactors.attemptToAdd(thisFactor);
				for(int i = 0; i < linkIds.length; ++i)
				{
					FactorLink thisLinkage = factorLinkPool.find(linkIds[i]);
					processLink(unprocessedFactors, thisFactor, thisLinkage, direction);
				}
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
		
		FactorLinkPool factorLinkPool = getProject().getFactorLinkPool();
		for(int i = 0; i < factorLinkPool.getFactorLinkIds().length; ++i)
		{
			FactorLink thisLink = factorLinkPool.find(factorLinkPool.getFactorLinkIds()[i]);
			processLink(results, startingFactor, thisLink, direction);
		}
		return results;
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
	
	HashMap<ORef, FactorSet> cachedUpstreamChain;
	HashMap<ORef, FactorSet> cachedDownstreamChain;
}
