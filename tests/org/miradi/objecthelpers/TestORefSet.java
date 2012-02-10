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
package org.miradi.objecthelpers;

import org.martus.util.TestCaseEnhanced;
import org.miradi.ids.BaseId;
import org.miradi.objects.Indicator;

public class TestORefSet extends TestCaseEnhanced
{
	public TestORefSet(String name)
	{
		super(name);
	}

	public void testSubtract()
	{
		ORef inLeft = new ORef(1, new BaseId(6));
		ORef inRight = new ORef(2, new BaseId(17));
		ORef inBoth = new ORef(3, new BaseId(99));
		
		ORefSet left = new ORefSet();
		left.add(inLeft);
		left.add(inBoth);
		
		ORefSet right = new ORefSet();
		right.add(inRight);
		right.add(inBoth);
		
		ORefSet leftOnly = ORefSet.subtract(left, right);
		assertEquals(1, leftOnly.size());
		assertContains(inLeft, leftOnly);
		
		ORefSet rightOnly = ORefSet.subtract(right, left);
		assertEquals(1, rightOnly.size());
		assertContains(inRight, rightOnly);
	}
	
	public void testGetNonOverlappingRefs()
	{
		ORefSet orefSet1 = new ORefSet();
		ORefSet orefSet2 = new ORefSet();
		assertTrue("Two empty list should have no nonOverlapping refs?", ORefSet.getNonOverlappingRefs(orefSet1, orefSet2).isEmpty());
		assertTrue("Two empty list should have no nonOverlapping refs?", ORefSet.getNonOverlappingRefs(orefSet2, orefSet1).isEmpty());
		
		ORef nonOverlappingRef1 = new ORef(Indicator.getObjectType(), new BaseId(9999));
		orefSet1.add(nonOverlappingRef1);
		assertTrue("Should have one non overlapping ref?", ORefSet.getNonOverlappingRefs(orefSet1, orefSet2).hasData());
		assertTrue("Should have one non overlapping ref?", ORefSet.getNonOverlappingRefs(orefSet2, orefSet1).hasData());
		
		ORef nonOverlappingRef2 = new ORef(Indicator.getObjectType(), new BaseId(8888));
		orefSet2.add(nonOverlappingRef2);
		assertTrue("Should have two non overlapping ref?", ORefSet.getNonOverlappingRefs(orefSet1, orefSet2).hasData());
		assertTrue("Should have two non overlapping ref?", ORefSet.getNonOverlappingRefs(orefSet2, orefSet1).hasData());
		ORefSet nonOverlappingRefsFor1And2 = ORefSet.getNonOverlappingRefs(orefSet1, orefSet2);
		assertTrue("should contain non overlapping ref?", nonOverlappingRefsFor1And2.contains(nonOverlappingRef1));
		assertTrue("should contain non overlapping ref?", nonOverlappingRefsFor1And2.contains(nonOverlappingRef2));
		
		ORefSet nonOverlappingRefsFor2And1 = ORefSet.getNonOverlappingRefs(orefSet2, orefSet1);
		assertTrue("should contain non overlapping ref?", nonOverlappingRefsFor2And1.contains(nonOverlappingRef1));
		assertTrue("should contain non overlapping ref?", nonOverlappingRefsFor2And1.contains(nonOverlappingRef2));
	}
}
