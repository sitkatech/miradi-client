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
		/* 
		 * NOTE: The following classes are pulled from jgraphaddons:
		 * JGraphEllipseView;
		 * JGraphRoundRectView;
		 * JGraphDiamondView;
		 * JGraphMultilineView;
		 */
//			if(v instanceof FlexibleCell)
//			{
//				FlexibleCell cell = (FlexibleCell)v;
//				if(cell.isEllipse())
//				{
//					//return new JGraphEllipseView(v);
//					return new EllipseCellView(v);
//				}
//				else if(cell.isHexagon())
//				{
//					//return new JGraphRoundRectView(v);
//					return new HexagonCellView(v);
//				}
//				else if(cell.isTriangle())
//				{
//					return new TriangleCellView(v);
//				}
//				else if(cell.isThreat())
//				{
//					return new ThreatCellView(v);
//				}
//			}

		//return super.createVertexView(v);
		//return new MultilineCellView(v);
		return new RectangleNodeView(v);
	}
}