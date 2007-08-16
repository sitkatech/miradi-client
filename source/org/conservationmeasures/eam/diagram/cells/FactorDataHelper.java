/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
//TODO place this class in the better fit package
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;

import org.conservationmeasures.eam.project.Project;

//TODO come up with a better name
public class FactorDataHelper 
{
	public FactorDataHelper(Point insertionPointToUse, Point upperMostLeftMostCornerToUse)
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
			return new Point(0, 0);
		
		Point translatedPoint = new Point(point);
		translatedPoint.translate(- upperMostLeftMostCorner.x, - upperMostLeftMostCorner.y);

		return translatedPoint;	
	}
	
	Point insertionPoint;
	Point upperMostLeftMostCorner;
}
