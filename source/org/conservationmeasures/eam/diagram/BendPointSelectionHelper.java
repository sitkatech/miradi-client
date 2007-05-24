/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Vector;

public class BendPointSelectionHelper
{
	public BendPointSelectionHelper()
	{
		selectionList = new Vector();
	}
	
	public void mousePressed(MouseEvent mouseEvent, Point currentBendPoint)
	{	
		if (canAdd(mouseEvent, currentBendPoint))
			addToSelection(currentBendPoint);
		
		else if (canRemove(mouseEvent, currentBendPoint))
			removeFromSelection(currentBendPoint);
	}

	public void addToSelection(Point pointToAdd)
	{
		selectionList.add(pointToAdd);
	}
	
	public void removeFromSelection(Point pointToRemove)
	{
		selectionList.remove(pointToRemove);
	}
	
	public boolean canRemove(MouseEvent mouseEvent, Point clickPoint)
	{
		if( clickPoint == null)
			return false;
		
		return selectionList.contains(clickPoint);
	}
	
	public boolean canAdd(MouseEvent mouseEvent, Point clickPoint)
	{
		if( clickPoint == null)
			return false;
		
		return ! selectionList.contains(clickPoint);
	}
	
	public Point2D[] getSelectionList()
	{
		return (Point2D[]) selectionList.toArray(new Point2D[0]);
	}
	
	Vector selectionList;
}
