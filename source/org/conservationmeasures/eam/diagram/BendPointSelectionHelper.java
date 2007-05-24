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
		if (shouldAdd(mouseEvent, currentBendPoint))
			selectionList.add(currentBendPoint);
		
		else if (shouldRemove(mouseEvent, currentBendPoint))
			selectionList.remove(currentBendPoint);
	}

	public boolean shouldRemove(MouseEvent mouseEvent, Point clickPoint)
	{
		if( clickPoint == null)
			return false;
		
		return selectionList.contains(clickPoint);
	}
	
	public boolean shouldAdd(MouseEvent mouseEvent, Point clickPoint)
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
