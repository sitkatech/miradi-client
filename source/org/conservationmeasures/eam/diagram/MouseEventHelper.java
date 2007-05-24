/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;

public class MouseEventHelper
{
	public boolean isCtrlDown()
	{
		return ctrlDown;
	}

	public void setCtrlDown(boolean ctrlDownToUse)
	{
		ctrlDown = ctrlDownToUse;
	}

	public boolean isShiftDown()
	{
		return shiftDown;
	}

	public void setShiftDown(boolean shiftDownToUse)
	{
		shiftDown = shiftDownToUse;
	}

	public void setClickPoint(Point clickPointToUse)
	{
		clickPoint = clickPointToUse;
	}
	
	public Point getClickPoint()
	{
		return clickPoint;
	}
	
	Point clickPoint;
	boolean shiftDown;
	boolean ctrlDown;
}
