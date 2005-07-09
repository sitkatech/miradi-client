/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.geom.Point2D;

import org.conservationmeasures.eam.main.EAM;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;


public class MultilineNodeView extends VertexView
{
    public MultilineNodeView(Object cell) 
    {
        super(cell);
    }

	public Point2D getPerimeterPoint(EdgeView arg0, Point2D arg1, Point2D arg2)
	{
		EAM.logWarning("MultilineNodeView.getPerimeterPoint not implemented!");
		return super.getPerimeterPoint(arg0, arg1, arg2);
	}
    
}
