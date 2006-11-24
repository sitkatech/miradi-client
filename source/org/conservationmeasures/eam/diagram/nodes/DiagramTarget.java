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
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Target;

public class DiagramTarget extends DiagramNode
{
	public DiagramTarget(DiagramNodeId idToUse, Target cmTarget)
	{
		super(idToUse, cmTarget);
	}

	public Color getColor()
	{
		return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_TARGET);
	}
	
	public Dimension getInsetDimension()
	{
		Rectangle boundingBox = new Rectangle(getSize());
		Point insideUpperLeftCorner = EllipseRenderer.getPerimeterPoint(new Point(0, 0), boundingBox);
		return new Dimension(insideUpperLeftCorner.x, insideUpperLeftCorner.y);
	}


}
