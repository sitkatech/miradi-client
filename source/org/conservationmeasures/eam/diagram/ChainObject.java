/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class ChainObject
{
	public ConceptualModelNodeSet getNodes()
	{
		return cmNodeSet;
	}
	
	public ConceptualModelLinkage[] getLinkages()
	{
		ConceptualModelLinkage[] cmLinks = (ConceptualModelLinkage[])processedLinks.toArray(new ConceptualModelLinkage[0]);
		return cmLinks;
	} 
		
	public void buildDirectThreatChain(DiagramModel model, ConceptualModelNode node)
	{
		initializeChain(model, node);
		if(startingNode.isDirectThreat())
		{
			cmNodeSet.attemptToAddAll(getDirectlyLinkedDownstreamNodes());
			cmNodeSet.attemptToAddAll(getAllUpstreamNodes());
		}
	}

	public void buildNormalChain(DiagramModel model, ConceptualModelNode node)
	{
		initializeChain(model, node);
		if (startingNode.isDirectThreat())
			buildDirectThreatChain(model, node);
		else
			buildUpstreamDownstreamChain(model, node);
	}
	
	public void buildUpstreamDownstreamChain(DiagramModel model, ConceptualModelNode node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getAllDownstreamNodes());
		cmNodeSet.attemptToAddAll(getAllUpstreamNodes());
	}
	
	public void buildUpstreamChain(DiagramModel model, ConceptualModelNode node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getAllUpstreamNodes());
	}
	
	public void buildDownstreamChain(DiagramModel model, ConceptualModelNode node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getAllDownstreamNodes());
	}
	
	public void buidDirectlyLinkedDownstreamChain(DiagramModel model, ConceptualModelNode node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getDirectlyLinkedDownstreamNodes());
	}
	
	public void buildDirectlyLinkedUpstreamChain(DiagramModel model, ConceptualModelNode node)
	{
		initializeChain(model, node);
		cmNodeSet.attemptToAddAll(getDirectlyLinkedUpstreamNodes());
	}
	
	private ConceptualModelNodeSet getDirectlyLinkedDownstreamNodes()
	{
		return getDirectlyLinkedNodes(ConceptualModelLinkage.FROM);
	}
	
	private ConceptualModelNodeSet getDirectlyLinkedUpstreamNodes()
	{
		return getDirectlyLinkedNodes(ConceptualModelLinkage.TO);
	}
	
	private ConceptualModelNodeSet getAllUpstreamNodes()
	{
		return getAllLinkedNodes(ConceptualModelLinkage.TO);
	}

	private ConceptualModelNodeSet getAllDownstreamNodes()
	{
		return getAllLinkedNodes(ConceptualModelLinkage.FROM);
	}
	
	private ConceptualModelNodeSet getAllLinkedNodes(int direction)
	{
		ConceptualModelNodeSet linkedNodes = new ConceptualModelNodeSet();
		ConceptualModelNodeSet unprocessedNodes = new ConceptualModelNodeSet();
		linkedNodes.attemptToAdd(startingNode);
		LinkagePool linkagePool = diagramModel.getLinkagePool();
		
		ModelLinkageId[] linkagePoolIds = linkagePool.getModelLinkageIds();
		for(int i = 0; i < linkagePoolIds.length; ++i)
		{
			ConceptualModelLinkage thisLinkage = linkagePool.find(linkagePoolIds[i]);
			if(thisLinkage.getNodeId(direction).equals(startingNode.getId()))
			{
				attempToAdd(thisLinkage);
				ConceptualModelNode linkedNode = diagramModel.getNodePool().find(thisLinkage.getOppositeNodeId(direction));
				unprocessedNodes.attemptToAdd(linkedNode);
			}
		}		
		
		while(unprocessedNodes.size() > 0)
		{
			ConceptualModelNode thisNode = (ConceptualModelNode)unprocessedNodes.toArray()[0];
			linkedNodes.attemptToAdd(thisNode);
			for(int i = 0; i < linkagePoolIds.length; ++i)
			{
				ConceptualModelLinkage thisLinkage = linkagePool.find(linkagePoolIds[i]);
				if(thisLinkage.getNodeId(direction).equals(thisNode.getId()))
				{
					attempToAdd(thisLinkage);
					ConceptualModelNode linkedNode = diagramModel.getNodePool().find(thisLinkage.getOppositeNodeId(direction));
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
			ConceptualModelLinkage thisLinkage = linkagePool.find(linkagePool.getModelLinkageIds()[i]);
			if(thisLinkage.getNodeId(direction).equals(startingNode.getId()))
			{
				attempToAdd(thisLinkage);
				ModelNodeId downstreamNodeId = thisLinkage.getOppositeNodeId(direction);
				ConceptualModelNode downstreamNode = diagramModel.getNodePool().find(downstreamNodeId);
				results.attemptToAdd(downstreamNode);
			}
		}
		return results;
	}
	
	private void initializeChain(DiagramModel model, ConceptualModelNode node)
	{
		this.diagramModel = model;
		this.startingNode = node;
		cmNodeSet = new ConceptualModelNodeSet();
		processedLinks = new Vector();
	}
	
	private void attempToAdd(ConceptualModelLinkage thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}

	private ConceptualModelNodeSet cmNodeSet;
	private ConceptualModelNode startingNode;
	private DiagramModel diagramModel;
	private Vector processedLinks;
}
