/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

public class CellViewFactory extends DefaultCellViewFactory
{
	protected VertexView createVertexView(Object view)
	{
		Node node = (Node)view;
		if(node.isGoal())
		{
			return new EllipseNodeView(node);
		}
		if(node.isThreat())
		{
			return new RectangleNodeView(node);
		}
		if(node.isIntervention())
		{
			return new HexagonNodeView(node);
		}
		
		throw new RuntimeException("Unknown node type");
	}
}