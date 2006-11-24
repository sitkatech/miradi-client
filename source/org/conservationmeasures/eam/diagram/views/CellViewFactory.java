/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.views;

import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.DiagramFactorCluster;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;

public class CellViewFactory extends DefaultCellViewFactory
{
	protected VertexView createVertexView(Object view)
	{
		EAMGraphCell cell = (EAMGraphCell)view;
		if(cell.isNode())
			return createNodeView(view);
		
		return new ProjectScopeView(view);
	}

	private VertexView createNodeView(Object view)
	{
		DiagramNode node = (DiagramNode)view;
		if(node.isTarget())
		{
			return new EllipseNodeView(node);
		}
		if(node.isDirectThreat())
		{
			return new RectangleNodeView(node);
		}
		if(node.isStress())
		{
			return new RectangleNodeView(node);
		}
		if(node.isIndirectFactor())
		{
			return new RectangleNodeView(node);
		}
		if(node.isIntervention())
		{
			return new HexagonNodeView(node);
		}
		if(node.isCluster())
		{
			return new ClusterView((DiagramFactorCluster)node);
		}
		throw new RuntimeException("Unknown node type");
	}

	protected EdgeView createEdgeView(Object edge)
	{
		return new LinkageView(edge);
	}
}