/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.conservationmeasures.eam.diagram.renderers.EllipseRenderer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Target;

public class DiagramTargetCell extends FactorCell
{
	public DiagramTargetCell(Target targetToWrap, DiagramFactor diagramFactor)
	{
		super(targetToWrap, diagramFactor);
	}

	public Color getColor()
	{
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_TARGET);
	}
	
	public Dimension getInsetDimension()
	{
		Rectangle boundingBox = new Rectangle(getSize());
		Point insideUpperLeftCorner = EllipseRenderer.getPerimeterPoint(new Point(0, 0), boundingBox);
		return new Dimension(insideUpperLeftCorner.x, insideUpperLeftCorner.y);
	}


}
