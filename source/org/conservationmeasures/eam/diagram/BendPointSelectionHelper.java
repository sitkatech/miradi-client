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

import org.conservationmeasures.eam.utils.Utility;

public class BendPointSelectionHelper
{
	public BendPointSelectionHelper()
	{
		clearSelection();
	}
	
	public void clearSelection()
	{
		selectionList = new Vector();	
	}
	
	public void mousePressed(MouseEvent mouseEvent, Point2D currentBendPoint)
	{	
		updateSelectionList(mouseEvent, currentBendPoint);			
	}

	public void updateSelectionList(MouseEvent mouseEvent, Point2D point2D)
	{
		if (point2D == null)
			return;
		//TODO all isShiftDown()s should be isControlDown()s
		Point point = Utility.convertToPoint(point2D);
		if (! selectionList.contains(point) && ! mouseEvent.isShiftDown())
		{
			clearSelection();
			addToSelection(point);
			return;
		}
		
		if (! selectionList.contains(point) && mouseEvent.isShiftDown())
		{
			addToSelection(point);
			return;
		}
		
		if (selectionList.contains(point) && mouseEvent.isShiftDown())
		{
			removeFromSelection(point);
			return;
		}
		
		if (selectionList.contains(point) && ! mouseEvent.isShiftDown())
		{
			clearSelection();
			addToSelection(point);
			return;
		}
		
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
