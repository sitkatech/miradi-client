/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;

//FIXME: ProjectChainObject and DiagramChainObject should be have a common super class for dup code
public class ProjectChainObject  extends ChainObject
{
	public void buildDirectThreatChain(Factor factor)
	{
		initializeChain(factor);
		if(startingFactor.isDirectThreat())
		{
			factorSet.attemptToAddAll(getDirectlyLinkedDownstreamFactors());
			factorSet.attemptToAddAll(getAllUpstreamFactors());
		}
	}

	public void buildNormalChain(Factor factor)
	{
		initializeChain(factor);
		if (startingFactor.isDirectThreat())
			buildDirectThreatChain(factor);
		else
			buildUpstreamDownstreamChain(factor);
	}
	
	public void buildUpstreamDownstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	public void buildUpstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	public void buildDownstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
	}
	
	public void buidDirectlyLinkedDownstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getDirectlyLinkedDownstreamFactors());
	}
	
	public void buildDirectlyLinkedUpstreamChain(Factor factor)
	{
		initializeChain(factor);
		factorSet.attemptToAddAll(getDirectlyLinkedUpstreamFactors());
	}
	
	public FactorSet getAllLinkedFactors(int direction)
	{
		FactorSet linkedFactors = new FactorSet();
		FactorSet unprocessedFactors = new FactorSet();
		linkedFactors.attemptToAdd(startingFactor);
		FactorLinkPool factorLinkPool = getObjectManager().getLinkagePool();
		
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
		
		return linkedFactors;
	}

	private void processLink(FactorSet unprocessedFactors, Factor thisFactor, FactorLink thisLink, int direction)
	{
		if(thisLink.getNodeId(direction).equals(thisFactor.getId()))
		{
			attempToAdd(thisLink);
			Factor linkedNode = getObjectManager().findNode(thisLink.getOppositeNodeId(direction));
			unprocessedFactors.attemptToAdd(linkedNode);
			return;
		}
		
		if (!thisLink.isBidirectional())
			return;
		
		if(thisLink.getOppositeNodeId(direction).equals(thisFactor.getId()))
		{
			attempToAdd(thisLink);
			Factor linkedNode = getObjectManager().findNode(thisLink.getNodeId(direction));
			unprocessedFactors.attemptToAdd(linkedNode);
		}
	}
	
	public FactorSet getDirectlyLinkedFactors(int direction)
	{
		FactorSet results = new FactorSet();
		results.attemptToAdd(startingFactor);
		
		FactorLinkPool factorLinkPool = getObjectManager().getLinkagePool();
		for(int i = 0; i < factorLinkPool.getFactorLinkIds().length; ++i)
		{
			FactorLink thisLink = factorLinkPool.find(factorLinkPool.getFactorLinkIds()[i]);
			processLink(results, startingFactor, thisLink, direction);
		}
		return results;
	}
	
	private void initializeChain(Factor factor)
	{
		this.startingFactor = factor;
		factorSet = new FactorSet();
		processedLinks = new Vector();
	}
	
	private void attempToAdd(FactorLink thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}
	
	private ObjectManager getObjectManager()
	{
		return startingFactor.getObjectManager();
	}

	private Factor startingFactor;
}
