/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.conservationmeasures.eam.diagram.renderers.EllipseRenderer;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;

public class DiagramTarget extends DiagramNode
{
	public DiagramTarget(ConceptualModelTarget cmTarget)
	{
		super(cmTarget);
	}

	public Color getColor()
	{
		return DiagramConstants.COLOR_TARGET;
	}
	
	public Dimension getInsetDimension()
	{
		Rectangle boundingBox = new Rectangle(getSize());
		Point insideUpperLeftCorner = EllipseRenderer.getPerimeterPoint(new Point(0, 0), boundingBox);
		return new Dimension(insideUpperLeftCorner.x, insideUpperLeftCorner.y);
	}

	public Rectangle getAnnotationsRect()
	{
		return getAnnotationsRect(getGoals().size());
	}


}
