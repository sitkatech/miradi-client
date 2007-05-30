/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class TestBendPointList extends TestPointList
{
	public TestBendPointList(String name)
	{
		super(name);
	}
	
	public void testCreateLineSegment()
	{
		BendPointList bendPointList = new BendPointList();
		Point2D point1 = new Point2D.Double(0, 0);
		Point2D point2 = new Point2D.Double(10, 0);
		Line2D expectedLine = new Line2D.Double(point1, point2);
		
		Line2D createdLine = bendPointList.createLineSegment(point1, point2);
		//TODO double to make sure this bounds test comparison is enough
		assertEquals("not same line?", expectedLine.getBounds(), createdLine.getBounds());
	}
}
