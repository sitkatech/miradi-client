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
	protected VertexView createVertexView(Object v)
	{
		Node node = (Node)v;
		/* 
		 * NOTE: The following classes are pulled from jgraphaddons:
		 * JGraphEllipseView;
		 * JGraphRoundRectView;
		 * JGraphDiamondView;
		 * JGraphMultilineView;
		 */
		if(node.isGoal())
		{
			return new EllipseNodeView(node);
		}
		if(node.isThreat())
		{
			return new RectangleNodeView(node);
		}
		
		throw new RuntimeException("Unknown node type");
	}
}