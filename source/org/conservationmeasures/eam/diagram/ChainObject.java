/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;

public class ChainObject
{
	public ConceptualModelNodeSet getNodes()
	{
		return cmNodeSet;
	}
	
	public Factor[] getNodesArray()
	{	
		Factor[] cmNodes = (Factor[])cmNodeSet.toArray(new Factor[0]);
		return cmNodes;
	}
	
	public FactorLink[] getLinkages()
	{
		FactorLink[] cmLinks = (FactorLink[])processedLinks.toArray(new FactorLink[0]);
		return cmLinks;
	} 
		
	public void buildDirectThreatChain(DiagramModel model, Factor node)
	{
		initializeChain(model, node);
		if(startingNode.isDirectThreat())
		{
			cmNodeSet.attemptToAddAll(getDirectlyLinkedDownstreamNodes());
			cmNodeSet.attemptToAddAll(getAllUpstreamNodes());
		}
	}

	public void buildNormalChain(DiagramModel model, Factor node)
	{
		initializeChain(model, node);
		if (startingNode.isDirectThreat())
			buildDirectThreatChain(model, node);
		else
			buildUpstreamDownstreamChain(model, node);
	}
	
	public void buildUpstreamDownstreamChain(DiagramModel model, Factor node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getAllDownstreamNodes());
		cmNodeSet.attemptToAddAll(getAllUpstreamNodes());
	}
	
	public void buildUpstreamChain(DiagramModel model, Factor node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getAllUpstreamNodes());
	}
	
	public void buildDownstreamChain(DiagramModel model, Factor node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getAllDownstreamNodes());
	}
	
	public void buidDirectlyLinkedDownstreamChain(DiagramModel model, Factor node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getDirectlyLinkedDownstreamNodes());
	}
	
	public void buildDirectlyLinkedUpstreamChain(DiagramModel model, Factor node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getDirectlyLinkedUpstreamNodes());
	}
	
	private ConceptualModelNodeSet getDirectlyLinkedDownstreamNodes()
	{
		return getDirectlyLinkedNodes(FactorLink.FROM);
	}
	
	private ConceptualModelNodeSet getDirectlyLinkedUpstreamNodes()
	{
		return getDirectlyLinkedNodes(FactorLink.TO);
	}
	
	private ConceptualModelNodeSet getAllUpstreamNodes()
	{
		return getAllLinkedNodes(FactorLink.TO);
	}

	private ConceptualModelNodeSet getAllDownstreamNodes()
	{
		return getAllLinkedNodes(FactorLink.FROM);
	}
	
	private ConceptualModelNodeSet getAllLinkedNodes(int direction)
	{
		ConceptualModelNodeSet linkedNodes = new ConceptualModelNodeSet();
		ConceptualModelNodeSet unprocessedNodes = new ConceptualModelNodeSet();
		linkedNodes.attemptToAdd(startingNode);
		LinkagePool linkagePool = diagramModel.getLinkagePool();
		
		FactorLinkId[] linkagePoolIds = linkagePool.getModelLinkageIds();
		for(int i = 0; i < linkagePoolIds.length; ++i)
		{
			FactorLink thisLinkage = linkagePool.find(linkagePoolIds[i]);
			if(thisLinkage.getNodeId(direction).equals(startingNode.getId()))
			{
				attempToAdd(thisLinkage);
				Factor linkedNode = diagramModel.getNodePool().find(thisLinkage.getOppositeNodeId(direction));
				unprocessedNodes.attemptToAdd(linkedNode);
			}
		}		
		
		while(unprocessedNodes.size() > 0)
		{
			Factor thisNode = (Factor)unprocessedNodes.toArray()[0];
			linkedNodes.attemptToAdd(thisNode);
			for(int i = 0; i < linkagePoolIds.length; ++i)
			{
				FactorLink thisLinkage = linkagePool.find(linkagePoolIds[i]);
				if(thisLinkage.getNodeId(direction).equals(thisNode.getId()))
				{
					attempToAdd(thisLinkage);
					Factor linkedNode = diagramModel.getNodePool().find(thisLinkage.getOppositeNodeId(direction));
					unprocessedNodes.attemptToAdd(linkedNode);
				}
			}
			unprocessedNodes.remove(thisNode);
		}
		
		return linkedNodes;
	}

	private ConceptualModelNodeSet getDirectlyLinkedNodes(int direction)
	{
		ConceptualModelNodeSet results = new ConceptualModelNodeSet();
		results.attemptToAdd(startingNode);
		
		LinkagePool linkagePool = diagramModel.getLinkagePool();
		for(int i = 0; i < linkagePool.getModelLinkageIds().length; ++i)
		{
			FactorLink thisLinkage = linkagePool.find(linkagePool.getModelLinkageIds()[i]);
			if(thisLinkage.getNodeId(direction).equals(startingNode.getId()))
			{
				attempToAdd(thisLinkage);
				FactorId downstreamNodeId = thisLinkage.getOppositeNodeId(direction);
				Factor downstreamNode = diagramModel.getNodePool().find(downstreamNodeId);
				results.attemptToAdd(downstreamNode);
			}
		}
		return results;
	}
	
	private void initializeChain(DiagramModel model, Factor node)
	{
		this.diagramModel = model;
		this.startingNode = node;
		cmNodeSet = new ConceptualModelNodeSet();
		processedLinks = new Vector();
	}
	
	private void attempToAdd(FactorLink thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}

	private ConceptualModelNodeSet cmNodeSet;
	private Factor startingNode;
	private DiagramModel diagramModel;
	private Vector processedLinks;
}
