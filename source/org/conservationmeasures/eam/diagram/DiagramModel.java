/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeGoal;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeThreat;
import org.jgraph.graph.DefaultGraphModel;

public class DiagramModel extends DefaultGraphModel
{
	public DiagramModel()
	{
	}
	
	public void removeAll()
	{
		while(getRootCount() > 0)
			remove(new Object[] {getRootAt(0)});
	}

	public Object createGoalNode()
	{
		Node node = new Node(new NodeTypeGoal());
		insertNode(node);
		return node;
	}
	
	public Object createThreatNode()
	{
		Node node = new Node(new NodeTypeThreat());
		insertNode(node);
		return node;
	}
	
	public Object createInterventionNode()
	{
		Node node = new Node(new NodeTypeIntervention());
		insertNode(node);
		return node;
	}
	
	public void insertNode(Node nodeToInsert)
	{
		Object[] nodes = new Object[] {nodeToInsert};
		insert(nodes, nodeToInsert.getNestedAttributeMap(), null, null, null);
	}
	
	public void updateNode(Node nodeToUpdate)
	{
		edit(nodeToUpdate.getNestedAttributeMap(), null, null, null);
	}

	public int getNodeId(Node node)
	{
		Object[] allNodes = getAll(this);
		for(int i = 0; i < allNodes.length; ++i)
			if(allNodes[i].equals(node))
				return i;
		
		return -1;
	}
	
	public Node getNodeById(int id)
	{
		return (Node)getAll(this)[id];
	}
}
