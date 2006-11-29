/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cellviews;

import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorCluster;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;

public class CellViewFactory extends DefaultCellViewFactory
{
	protected VertexView createVertexView(Object cell)
	{
		EAMGraphCell eamGraphCell = (EAMGraphCell)cell;
		if(eamGraphCell.isFactor())
			return createNodeView(eamGraphCell);
		
		return new ProjectScopeView(eamGraphCell);
	}

	private VertexView createNodeView(EAMGraphCell cell)
	{
		DiagramFactor diagramFactor = (DiagramFactor)cell;
		if(diagramFactor.isTarget())
		{
			return new EllipseFactorView(diagramFactor);
		}
		if(diagramFactor.isDirectThreat())
		{
			return new RectangleFactorView(diagramFactor);
		}
		if(diagramFactor.isStress())
		{
			return new RectangleFactorView(diagramFactor);
		}
		if(diagramFactor.isContributingFactor())
		{
			return new RectangleFactorView(diagramFactor);
		}
		if(diagramFactor.isStrategy())
		{
			return new HexagonFactorView(diagramFactor);
		}
		if(diagramFactor.isFactorCluster())
		{
			return new FactorClusterView((DiagramFactorCluster)diagramFactor);
		}
		throw new RuntimeException("Unknown node type");
	}

	protected EdgeView createEdgeView(Object edge)
	{
		return new FactorLinkView(edge);
	}
}