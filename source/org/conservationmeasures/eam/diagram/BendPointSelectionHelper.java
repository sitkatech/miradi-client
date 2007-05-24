/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class BendPointSelectionHelper
{
	public BendPointSelectionHelper()
	{
		selectionList = new Vector();
	}
	
	public void mousePressed(MouseEvent mouseEvent)
	{
		Point clickPoint = mouseEvent.getPoint();
		
		if (! shouldRemove(clickPoint))
			selectionList.add(clickPoint);
		
		else if (shouldRemove(clickPoint))
			selectionList.remove(clickPoint);
	}

	private boolean shouldRemove(Point clickPoint)
	{
		if( clickPoint == null)
			return false;
		
		return selectionList.contains(clickPoint);
	}
	
	Vector selectionList;
}
