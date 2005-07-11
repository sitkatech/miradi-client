/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

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
		cellInventory = new CellInventory();
	}
	
	public void removeAll()
	{
		while(getRootCount() > 0)
			remove(new Object[] {getRootAt(0)});
		cellInventory.removeAll();
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
	
	private void insertLinkage(Linkage linkageToInsert)
	{
		Object[] linkages = new Object[]{linkageToInsert};
		Map nestedMap = linkageToInsert.getNestedAttributeMap();
		ConnectionSet cs = linkageToInsert.getConnectionSet();
		insert(linkages, nestedMap, cs, null, null);
		cellInventory.add(linkageToInsert);
	}
	
	private void insertNode(Node nodeToInsert)
	{
		Object[] nodes = new Object[] {nodeToInsert};
		Hashtable nestedAttributeMap = nodeToInsert.getNestedAttributeMap();
		insert(nodes, nestedAttributeMap, null, null, null);
		cellInventory.add(nodeToInsert);
	}
	
	public void updateCell(EAMGraphCell nodeToUpdate)
	{
		edit(nodeToUpdate.getNestedAttributeMap(), null, null, null);
	}
	
	public boolean isNode(Object cell)
	{
		// TODO: Is there something cleaner we can do here?
		return (cell instanceof Node);
	}
	
	public int getCellId(EAMGraphCell cell)
	{
		return cellInventory.find(cell);
	}

	public int getNodeId(Node node)
	{
		return getCellId(node);
	}
	
	public int getLinkageId(Linkage linkage)
	{
		return getCellId(linkage);
	}
	
	public EAMGraphCell getCellById(int id)
	{
		return cellInventory.get(id);
	}

	public Node getNodeById(int id)
	{
		return (Node)getCellById(id);
	}

	public Linkage getLinkageById(int id)
	{
		return (Linkage)getCellById(id);
	}

	CellInventory cellInventory;
}

class CellInventory
{
	public CellInventory()
	{
		vector = new Vector();
	}
	
	public void add(EAMGraphCell cell)
	{
		vector.add(cell);
	}
	
	public EAMGraphCell get(int index)
	{
		return (EAMGraphCell)vector.get(index);
	}
	
	public int find(EAMGraphCell cell)
	{
		return vector.indexOf(cell);
	}
	
	public void removeAll()
	{
		vector.removeAllElements();
	}
	
	Vector vector;
}
