/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import java.awt.Point;

import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class PointData extends ObjectData
{
	public PointData(String tagToUse)
	{
		super(tagToUse);
		point = new Point(0, 0);
	}

	public Point getPoint()
	{
		return point;
	}
	
	public void setPoint(Point pointToUse)
	{
		point = pointToUse;
	}
	
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			point = new Point(0, 0);
			return;
		}
		
		point = EnhancedJsonObject.convertToPoint(newValue);
	}

	public String get()
	{
		if(point == null)
			return "";

		return EnhancedJsonObject.convertFromPoint(point);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof PointData))
			return false;
		
		PointData other = (PointData)rawOther;
		return point.equals(other.point);
	}

	public int hashCode()
	{
		return point.hashCode();
	}

	Point point;
}
