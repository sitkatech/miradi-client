/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class BendPointList extends PointList
{
	public BendPointList()
	{
		super();
	}
	
	public BendPointList(BendPointList copyFrom)
	{
		super(copyFrom);
	}
	
	public BendPointList(EnhancedJsonObject json) throws Exception
	{
		super(json);
	}
	
	public BendPointList(String listAsJsonString) throws Exception
	{
		super(new EnhancedJsonObject(listAsJsonString));
	}
	
	public Line2D.Double createLineSegment(Point2D fromBendPoint, Point2D toBendPoint)
	{
		Point point1 = Utility.convertToPoint(fromBendPoint);
		Point point2 = Utility.convertToPoint(toBendPoint);
		
		return new Line2D.Double(point1, point2);
	}
}
