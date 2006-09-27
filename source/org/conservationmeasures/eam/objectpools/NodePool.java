/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class NodePool extends EAMObjectPool
{
	public NodePool()
	{
		super(ObjectType.MODEL_NODE);
	}
	
	public void put(ConceptualModelNode node)
	{
		put(node.getModelNodeId(), node);
	}
	
	public ConceptualModelNode find(ModelNodeId id)
	{
		return (ConceptualModelNode)getRawObject(id);
	}
	
	public void remove(ModelNodeId id)
	{
		super.remove(id);
	}

	public ModelNodeId[] getModelNodeIds()
	{
		return (ModelNodeId[])new HashSet(getRawIds()).toArray(new ModelNodeId[0]);
	}

	public ConceptualModelNode[] getInterventions()
	{
		return getNodesOfType(new NodeTypeIntervention());
	}
	
	public ConceptualModelNode[] getDirectThreats()
	{
		Vector cmNodes = new Vector();
		ModelNodeId[] ids = getModelNodeIds();
		Arrays.sort(ids);
		for(int i = 0; i < ids.length; ++i)
		{
			ConceptualModelNode cmNode = (ConceptualModelNode)getRawObject(ids[i]);
			if(cmNode.isDirectThreat())
				cmNodes.add(cmNode);
		}
		return (ConceptualModelNode[])cmNodes.toArray(new ConceptualModelNode[0]);
	}

	public ConceptualModelNode[] getTargets()
	{
		return getNodesOfType(new NodeTypeTarget());
	}

	private ConceptualModelNode[] getNodesOfType(NodeType type)
	{
		Vector cmNodes = new Vector();
		ModelNodeId[] ids = getModelNodeIds();
		Arrays.sort(ids);
		for(int i = 0; i < ids.length; ++i)
		{
			ConceptualModelNode cmNode = (ConceptualModelNode)getRawObject(ids[i]);
			if(cmNode.getNodeType().equals(type))
				cmNodes.add(cmNode);
		}
		return (ConceptualModelNode[])cmNodes.toArray(new ConceptualModelNode[0]);
	}
	
}
