/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cellviews;

import org.conservationmeasures.eam.diagram.cells.DiagramFactorCluster;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
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
		DiagramFactor node = (DiagramFactor)view;
		if(node.isTarget())
		{
			return new EllipseFactorView(node);
		}
		if(node.isDirectThreat())
		{
			return new RectangleFactorView(node);
		}
		if(node.isStress())
		{
			return new RectangleFactorView(node);
		}
		if(node.isIndirectFactor())
		{
			return new RectangleFactorView(node);
		}
		if(node.isIntervention())
		{
			return new HexagonFactorView(node);
		}
		if(node.isCluster())
		{
			return new FactorClusterView((DiagramFactorCluster)node);
		}
		throw new RuntimeException("Unknown node type");
	}

	protected EdgeView createEdgeView(Object edge)
	{
		return new FactorLinkView(edge);
	}
}