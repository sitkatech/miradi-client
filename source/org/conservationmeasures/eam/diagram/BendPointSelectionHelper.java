/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.util.Vector;

public class BendPointSelectionHelper
{
	public BendPointSelectionHelper()
	{
		selectionList = new Vector();
	}
	
	public void mousePressed(Point pressedPoints)
	{
		if (! shouldRemove(pressedPoints))
			selectionList.add(pressedPoints);
		
		else if (shouldRemove(pressedPoints))
			selectionList.remove(pressedPoints);
	}

	private boolean shouldRemove(Point pressedPoints)
	{
		if( pressedPoints == null)
			return false;
		
		return selectionList.contains(pressedPoints);
	}
	
	Vector selectionList;
}
