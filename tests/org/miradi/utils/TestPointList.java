/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.utils;

import java.awt.Point;
import java.awt.Rectangle;

import org.miradi.main.EAMTestCase;

public class TestPointList extends EAMTestCase
{
	public TestPointList(String name)
	{
		super(name);
	}
	
	public void testBasics()
	{
		PointList list = new PointList();
		assertEquals("wrong initial size?", 0, list.size());
		Point point1 = new Point(1, 1);
		Point point2 = new Point(2, 2);
		list.add(point1);
		list.add(point2);
		assertEquals("wrong size?", 2, list.size());
		assertEquals("bad get 1?", point1, list.get(0));
		assertEquals("bad get 2?", point2, list.get(1));
		
		list.removePoint(0);
		assertEquals("wrong size?", 1, list.size());
		assertEquals("bad get 1?", point2, list.get(0));
	}
	
	public void testJson() throws Exception
	{
		PointList list = createSamplePointList();
		EnhancedJsonObject json = list.toJson();
		
		PointList loaded = new PointList(json);
		assertEquals("wrong size?", list.size(), loaded.size());
		for(int i = 0; i < list.size(); ++i)
			assertEquals("wrong member?", list.get(i), loaded.get(i));
	}
	
	public void testEquals()
	{
		PointList list = createSamplePointList();
		PointList identical = createSamplePointList();
		assertEquals(list, identical);
		assertEquals(list.hashCode(), identical.hashCode());
		
		PointList different = new PointList();
		different.add(list.get(0));
		different.add(list.get(2));
		different.add(list.get(1));
		assertNotEquals("didn't compare order?", list, different);
		assertNotEquals("didn't hash everything?", list.hashCode(), different.hashCode());
		
		assertNotEquals("didn't check type?", list, new Object());
	}
	
	public void testSubtract()
	{
		PointList list12345 = new PointList();
		list12345.add(new Point(1, 1));
		list12345.add(new Point(2, 2));
		list12345.add(new Point(3, 3));
		list12345.add(new Point(4, 4));
		list12345.add(new Point(5, 5));
		
		PointList list654 = new PointList();
		list654.add(new Point(6, 6));
		list654.add(new Point(5, 5));
		list654.add(new Point(4, 4));
		
		PointList list123 = new PointList(list12345);
		list123.subtract(list654);
		assertEquals(3, list123.size());
		assertEquals(new Point(1, 1), list123.get(0));
		assertEquals(new Point(2, 2), list123.get(1));
		assertEquals(new Point(3, 3), list123.get(2));
		
		PointList list6 = new PointList(list654);
		list6.subtract(list12345);
		assertEquals(1, list6.size());
		assertEquals(new Point(6, 6), list6.get(0));
	}
	
	public void testFind()
	{
		Point[] points = new Point[] { new Point(1, 1), new Point(19, 19), new Point(3, 3), };
		PointList list = new PointList();
		
		for(int i = 0; i < points.length; ++i)
			list.add(points[i]);
		
		for(int i = 0; i < points.length; ++i)
			assertEquals("Couldn't find " + i + "?", i, list.find(points[i]));
		
		assertEquals("Found non-existant?", -1, list.find(new Point(27, 27)));

	}


	
	
	private PointList createSamplePointList()
	{
		PointList list = new PointList();
		list.add(new Point(1, 1));
		list.add(new Point(2, 2));
		list.add(new Point(3, 3));
		
		return list;
	}
	
	public void testGetClosestPoint()
	{
		PointList sampleList = createSamplePointList();
		Point point = new Point(4, 4);
		assertEquals(sampleList.get(2), sampleList.getClosestPoint(point));
		
		Point point2 = new Point(0, 0);
		assertEquals(sampleList.get(0), sampleList.getClosestPoint(point2));
		
		assertEquals("not the same point?", sampleList.get(1), sampleList.getClosestPoint(sampleList.get(1)));
		
		PointList emptyList = new PointList();
		assertEquals(new Point(0, 0), emptyList.getClosestPoint(point2));
	}
	
	public void testPointsBounds()
	{
		PointList sampleList = createSamplePointList();
		Rectangle expectedRectangle = new Rectangle(1, 1,  2, 2);
		assertEquals("wrong bounds?", expectedRectangle, sampleList.getBounds());
		
		assertEquals("should be null bounds for empty point list?", null, new PointList().getBounds());
		
		PointList points = new PointList();
		points.add(new Point(1, 1));
		Rectangle expectedRectangle2 = new Rectangle(1, 1, 0, 0);
		assertEquals("wrong bounds for one point?", expectedRectangle2, points.getBounds());
	}
}
