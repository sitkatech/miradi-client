/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
//TODO place this class in the better fit package
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;
import java.util.HashMap;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.project.Project;

//TODO come up with a better name
public class FactorDataHelper 
{
	public FactorDataHelper(Point insertionPointToUse, Point upperMostLeftMostCornerToUse)
	{
		insertionPoint = insertionPointToUse;
		upperMostLeftMostCorner = upperMostLeftMostCornerToUse;
	}

	public void setOriginalLocation(DiagramFactorId originalNodeId, Point originalLocation)
	{
		mapNodeLocations.put(getKey(originalNodeId), originalLocation);
	}

	public Point getNewLocation(Point originalLocation)
	{
		Point validatedInsertionPoint = getValidatedInsertionPoint(insertionPoint);		
		Point delta = computeDeltas(validatedInsertionPoint);
		Point translatedPoint = new Point(originalLocation);
		translatedPoint.translate(delta.x, delta.y);
		
		return translatedPoint;
	}

	public Point getNewLocation(DiagramFactorId originalNodeId)
	{
		Point originalNodeLocation = (Point)mapNodeLocations.get(getKey(originalNodeId));
		
		return getNewLocation(originalNodeLocation);
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

	private Integer getKey(BaseId key)
	{
		return getKey(key.asInt());
	}

	private Integer getKey(int key) 
	{
		return new Integer(key);
	}
	
	private Point computeDeltas(Point point)
	{
		Point upperLeft = upperMostLeftMostCorner;
		if (upperLeft == null)
			return new Point(0, 0);
		
		Point translatedPoint = new Point(point);
		translatedPoint.translate(- upperLeft.x, - upperLeft.y);

		return translatedPoint;	
	}
	
	HashMap mapNodeLocations = new HashMap();
	HashMap mapNodeIds = new HashMap();
	Point insertionPoint;
	Point upperMostLeftMostCorner;
}
