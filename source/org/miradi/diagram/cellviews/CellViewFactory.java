/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
		return createNodeView(eamGraphCell);
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
			return new RoundedRectangleFactorView(diagramFactor);
		}
		if(diagramFactor.isActivity())
		{
			return new RoundedRectangleFactorView(diagramFactor);
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
		if (diagramFactor.isScopeBox())
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