/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Hashtable;
import java.util.Map;

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeGoal;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeThreat;
import org.jgraph.graph.ConnectionSet;
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

	public Node createGoalNode()
	{
		Node node = new Node(new NodeTypeGoal());
		insertNode(node);
		return node;
	}
	
	public Node createThreatNode()
	{
		Node node = new Node(new NodeTypeThreat());
		insertNode(node);
		return node;
	}
	
	public Node createInterventionNode()
	{
		Node node = new Node(new NodeTypeIntervention());
		insertNode(node);
		return node;
	}
	
	public Linkage createLinkage(Node fromNode, Node toNode)
	{
		Linkage linkage = new Linkage(fromNode, toNode);
		insertLinkage(linkage);
		return linkage;
	}
	
	public void insertLinkage(Linkage linkageToInsert)
	{
		Object[] linkages = new Object[]{linkageToInsert};
		Map nestedMap = linkageToInsert.getNestedAttributeMap();
		ConnectionSet cs = linkageToInsert.getConnectionSet();
		insert(linkages, nestedMap, cs, null, null);
	}
	
	public void insertNode(Node nodeToInsert)
	{
		Object[] nodes = new Object[] {nodeToInsert};
		Hashtable nestedAttributeMap = nodeToInsert.getNestedAttributeMap();
		insert(nodes, nestedAttributeMap, null, null, null);
	}
	
	public void updateNode(EAMGraphCell nodeToUpdate)
	{
		edit(nodeToUpdate.getNestedAttributeMap(), null, null, null);
	}

	public int getNodeId(EAMGraphCell node)
	{
		Object[] allNodes = getAll(this);
		for(int i = 0; i < allNodes.length; ++i)
			if(allNodes[i].equals(node))
				return i;
		
		return -1;
	}
	
	public EAMGraphCell getNodeById(int id)
	{
		return (EAMGraphCell)getAll(this)[id];
	}
}
