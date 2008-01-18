/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.project.Project;

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
