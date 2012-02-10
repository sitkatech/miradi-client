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
package org.miradi.views.diagram;

import java.awt.Point;

import org.miradi.project.Project;

public class PointManipulater 
{
	public PointManipulater(Point insertionPointToUse, Point upperMostLeftMostCornerToUse)
	{
		insertionPoint = insertionPointToUse;
		upperMostLeftMostCorner = upperMostLeftMostCornerToUse;
	}

	public Point getNewLocation(Point originalLocation)
	{
		Point validatedInsertionPoint = getValidatedInsertionPoint(insertionPoint);		
		Point delta = computeDeltas(validatedInsertionPoint);
		Point translatedPoint = new Point(originalLocation);
		translatedPoint.translate(delta.x, delta.y);
		
		return translatedPoint;
	}

	public Point getSnappedTranslatedPoint(Project project, Point originalPoint, int offsetToAvoidOverlaying)
	{
		Point translatedSnappedPoint = getNewLocation(originalPoint);
		translatedSnappedPoint.translate(offsetToAvoidOverlaying, offsetToAvoidOverlaying);
		translatedSnappedPoint = project.getSnapped(translatedSnappedPoint);
		
		return translatedSnappedPoint;
	}

	public int getOffset(Project project)
	{
		if (insertionPoint != null)
			return 0;
		
		return project.getDiagramClipboard().getPasteOffset();
	}
	
	private Point getValidatedInsertionPoint(Point insertionPointToValidate)
	{
		if (insertionPointToValidate == null)
			return  upperMostLeftMostCorner;
		
		return insertionPointToValidate;
	}

	private Point computeDeltas(Point point)
	{
		if (upperMostLeftMostCorner == null)
			return point;
		
		Point translatedPoint = new Point(point);
		translatedPoint.translate(- upperMostLeftMostCorner.x, - upperMostLeftMostCorner.y);

		return translatedPoint;	
	}
	
	Point insertionPoint;
	Point upperMostLeftMostCorner;
}
