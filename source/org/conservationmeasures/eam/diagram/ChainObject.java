/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class ChainObject
{
	public ChainObject(DiagramModel diagramModel, ConceptualModelNode startingNode)
	{
		this.diagramModel = diagramModel;
		this.startingNode = startingNode;
		processedLinks = new Vector();
	}
	
	public ConceptualModelLinkage[] getLinksInChain()
	{
		ConceptualModelLinkage[] cmLinks = new ConceptualModelLinkage[processedLinks.size()];
		processedLinks.toArray(cmLinks);
		return cmLinks;
	}
	
	public ConceptualModelNodeSet getDirectThreatChainNodes()
	{
		ConceptualModelNodeSet results = new ConceptualModelNodeSet();
		if(!startingNode.isDirectThreat())
			return results;
		results.attemptToAddAll(getDirectlyLinkedDownstreamNodes());
		results.attemptToAddAll(getAllUpstreamNodes());
		
		return results;
	}
	
	public ConceptualModelNodeSet getNodesInChain()
	{
		if (startingNode.isDirectThreat())
			return getDirectThreatChainNodes();
		
		return getAllUpstreamDownstreamNodes();
	}
	
	public ConceptualModelNodeSet getDirectlyLinkedDownstreamNodes()
	{
		return getDirectlyLinkedNodes(ConceptualModelLinkage.FROM);
	}
	
	public ConceptualModelNodeSet getAllUpstreamDownstreamNodes()
	{
		ConceptualModelNodeSet results = new ConceptualModelNodeSet();
		results.attemptToAddAll(getAllDownstreamNodes());
		results.attemptToAddAll(getAllUpstreamNodes());
		
		return results;
	}
	
	public ConceptualModelNodeSet getAllUpstreamNodes()
	{
		return getAllLinkedNodes(ConceptualModelLinkage.TO);
	}

	public ConceptualModelNodeSet getAllDownstreamNodes()
	{
		return getAllLinkedNodes(ConceptualModelLinkage.FROM);
	}
	
	private ConceptualModelNodeSet getAllLinkedNodes(int direction)
	{
		ConceptualModelNodeSet linkedNodes = new ConceptualModelNodeSet();
		ConceptualModelNodeSet unprocessedNodes = new ConceptualModelNodeSet();
		linkedNodes.attemptToAdd(startingNode);
		LinkagePool linkagePool = diagramModel.getLinkagePool();
		
		BaseId[] linkagePoolIds = linkagePool.getIds();
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
		for(int i = 0; i < linkagePool.getIds().length; ++i)
		{
			ConceptualModelLinkage thisLinkage = linkagePool.find(linkagePool.getIds()[i]);
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
	
	private void attempToAdd(ConceptualModelLinkage thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}


	private ConceptualModelNode startingNode;
	private DiagramModel diagramModel;
	private Vector processedLinks;
}
