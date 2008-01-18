/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.conservationmeasures.eam.views.diagram.TestLinkBendPointsMoveHandler;

public class TestBendPointList extends TestPointList
{
	public TestBendPointList(String name)
	{
		super(name);
	}
	
	public void testCreateLineSegment()
	{
		PointList bendPointList = new PointList();
		Point2D point1 = new Point2D.Double(0, 0);
		Point2D point2 = new Point2D.Double(10, 0);
		Line2D expectedLine = new Line2D.Double(point1, point2);
		
		Line2D createdLine = bendPointList.createLineSegment(point1, point2);
		//TODO double to make sure this bounds test comparison is enough
		assertEquals("not same line?", expectedLine.getBounds(), createdLine.getBounds());
	}
	
	public void testConvertToLineSegments()
	{
		PointList bendPointList = TestLinkBendPointsMoveHandler.createBendPointList();
		Line2D.Double[] segments = bendPointList.convertToLineSegments();
		
		assertEquals("wrong number of segments?", 2, segments.length);
		assertEquals("wrong x1 for segment 1?", 1, (int)segments[0].getX1());
		assertEquals("wrong y1 for segment 1?", 1, (int)segments[0].getY1());
		
		assertEquals("wrong x2 for segment 1?", 2, (int)segments[0].getX2());
		assertEquals("wrong y2 for segment 1?", 2, (int)segments[0].getY2());
		
		assertEquals("wrong x1 for segment 2?", 2, (int)segments[1].getX1());
		assertEquals("wrong y1 for segment 2?", 2, (int)segments[1].getY1());
		
		assertEquals("wrong x2 for segment 2?", 3, (int)segments[1].getX2());
		assertEquals("wrong y2 for segment 2?", 3, (int)segments[1].getY2());
	}
}
