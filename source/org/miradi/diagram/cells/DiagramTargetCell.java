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
package org.miradi.diagram.cells;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.miradi.diagram.renderers.EllipseRenderer;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Target;

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
