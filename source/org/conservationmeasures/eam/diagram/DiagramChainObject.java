/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;

// TODO: Hopefully this can be eliminated when everyone 
// is using ProjectChainObject instead
public class DiagramChainObject
{
	public FactorSet getFactors()
	{
		return factorSet;
	}
	
	public Factor[] getFactorsArray()
	{	
		Factor[] cmNodes = (Factor[])factorSet.toArray(new Factor[0]);
		return cmNodes;
	}
	
	public FactorLink[] getFactorLinksArray()
	{
		FactorLink[] cmLinks = (FactorLink[])processedLinks.toArray(new FactorLink[0]);
		return cmLinks;
	} 
		
	public void buildDirectThreatChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		if(startingFactor.isDirectThreat())
		{
			factorSet.attemptToAddAll(getDirectlyLinkedDownstreamFactors());
			factorSet.attemptToAddAll(getAllUpstreamFactors());
		}
	}

	public void buildNormalChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		if (startingFactor.isDirectThreat())
			buildDirectThreatChain(model, factor);
		else
			buildUpstreamDownstreamChain(model, factor);
	}
	
	public void buildUpstreamDownstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	public void buildUpstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	public void buildDownstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
	}
	
	public void buidDirectlyLinkedDownstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getDirectlyLinkedDownstreamFactors());
	}
	
	public void buildDirectlyLinkedUpstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getDirectlyLinkedUpstreamFactors());
	}
	
	private FactorSet getDirectlyLinkedDownstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.FROM);
	}
	
	private FactorSet getDirectlyLinkedUpstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.TO);
	}
	
	private FactorSet getAllUpstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.TO);
	}

	private FactorSet getAllDownstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.FROM);
	}
	
	private FactorSet getAllLinkedFactors(int direction)
	{
		FactorSet linkedFactors = new FactorSet();
		FactorSet unprocessedFactors = new FactorSet();
		linkedFactors.attemptToAdd(startingFactor);
		FactorLinkPool factorLinkPool = diagramModel.getFactorLinkPool();
		
		FactorLinkId[] linkIds = factorLinkPool.getFactorLinkIds();
		for(int i = 0; i < linkIds.length; ++i)
		{
			FactorLink thisLink = factorLinkPool.find(linkIds[i]);
			if(thisLink.getNodeId(direction).equals(startingFactor.getId()))
			{
				attempToAdd(thisLink);
				Factor linkedFactor = diagramModel.getProject().findNode(thisLink.getOppositeNodeId(direction));
				unprocessedFactors.attemptToAdd(linkedFactor);
			}
		}		
		
		while(unprocessedFactors.size() > 0)
		{
			Factor thisFactor = (Factor)unprocessedFactors.toArray()[0];
			linkedFactors.attemptToAdd(thisFactor);
			for(int i = 0; i < linkIds.length; ++i)
			{
				FactorLink thisLinkage = factorLinkPool.find(linkIds[i]);
				if(thisLinkage.getNodeId(direction).equals(thisFactor.getId()))
				{
					attempToAdd(thisLinkage);
					Factor linkedNode = diagramModel.getProject().findNode(thisLinkage.getOppositeNodeId(direction));
					unprocessedFactors.attemptToAdd(linkedNode);
				}
			}
			unprocessedFactors.remove(thisFactor);
		}
		
		return linkedFactors;
	}

	private FactorSet getDirectlyLinkedFactors(int direction)
	{
		FactorSet results = new FactorSet();
		results.attemptToAdd(startingFactor);
		
		FactorLinkPool factorLinkPool = diagramModel.getFactorLinkPool();
		for(int i = 0; i < factorLinkPool.getFactorLinkIds().length; ++i)
		{
			FactorLink thisLink = factorLinkPool.find(factorLinkPool.getFactorLinkIds()[i]);
			if(thisLink.getNodeId(direction).equals(startingFactor.getId()))
			{
				attempToAdd(thisLink);
				FactorId downstreamFactorId = thisLink.getOppositeNodeId(direction);
				Factor downstreamFactor = diagramModel.getProject().findNode(downstreamFactorId);
				results.attemptToAdd(downstreamFactor);
			}
		}
		return results;
	}
	
	private void initializeChain(DiagramModel model, Factor factor)
	{
		this.diagramModel = model;
		this.startingFactor = factor;
		factorSet = new FactorSet();
		processedLinks = new Vector();
	}
	
	private void attempToAdd(FactorLink thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}

	private FactorSet factorSet;
	private Factor startingFactor;
	private DiagramModel diagramModel;
	private Vector processedLinks;
}
