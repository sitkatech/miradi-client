/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.cellviews;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;

public class CellViewFactory extends DefaultCellViewFactory
{
	protected VertexView createVertexView(Object cell)
	{
		// FIXME: We should have a MiradiVertexView that is the base 
		// class for all the others, which should include Rectangle, 
		// RoundedRectangle, Factor, etc.
		EAMGraphCell eamGraphCell = (EAMGraphCell)cell;
		if(eamGraphCell.isFactor())
			return createNodeView(eamGraphCell);
		
		return new ProjectScopeView(eamGraphCell);
	}

	private VertexView createNodeView(EAMGraphCell cell)
	{
		FactorCell diagramFactor = (FactorCell)cell;
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
		if (diagramFactor.isIntermediateResult())
		{
			return new RectangleFactorView(diagramFactor);
		}
		if (diagramFactor.isThreatRedectionResult())
		{
			return new RectangleFactorView(diagramFactor);
		}
		if (diagramFactor.isTextBox())
		{
			return new RoundedRectangleFactorView(diagramFactor);
		}
		if (diagramFactor.isGroupBox())
		{
			return new RoundedRectangleFactorView(diagramFactor);
		}
		
		throw new RuntimeException("Unknown node type " + diagramFactor.getWrappedType());
	}

	protected EdgeView createEdgeView(Object edge)
	{
		return new FactorLinkView(edge);
	}
}