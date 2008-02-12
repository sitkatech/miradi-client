/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.objecthelpers;

import org.miradi.ids.BaseId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.utils.EnhancedJsonObject;

public class TestORefList extends EAMTestCase
{
	public TestORefList(String name)
	{
		super(name);
	}

	public void testORefList()
	{
		ORefList objRefList = new ORefList();
		ORef objectRef = new ORef(ObjectType.GOAL, BaseId.INVALID);
		objRefList.add(objectRef);
		EnhancedJsonObject json = objRefList.toJson();
		ORefList got = new ORefList(json);
		assertEquals("lists are equal?", objRefList, got);
		
		ORef gotObjRef = objRefList.get(0);
		assertEquals("object references equal?", objectRef, gotObjRef);
	}
	
	public void testORefListJsonConstructor()
	{
//TODO	This test cannot pass because ORefList(json) constructor does a optArray and we dont enforce the json
//to contain refs only.  We can either enforce at the fieldTag level to make sure all tags end with ref
//		IdList idList = new IdList();
//		idList.add(new BaseId(10));
//		idList.add(new BaseId(11));
//		
//		try
//		{
//			new ORefList(idList.toString());
//			fail("should not be able to construct and orefList with wrong json");
//		}
//		catch(ParseException e)
//		{
//		}
	}
	
	public void testORefListExtract()
	{
		ORef objectRef1 = new ORef(ObjectType.GOAL, BaseId.INVALID);
		ORef objectRef2 = new ORef(ObjectType.GOAL, BaseId.INVALID);
		ORef objectRef3 = new ORef(ObjectType.TASK, BaseId.INVALID);
		ORefList objRefList = new ORefList(new ORef[] {objectRef1, objectRef2, objectRef3});
		ORefList goalList = objRefList.filterByType(ObjectType.GOAL);
		assertTrue(goalList.size()==2);
		assertTrue(goalList.contains(objectRef1));
		assertTrue(goalList.contains(objectRef2));
		assertFalse(goalList.contains(objectRef3));
	}
	
	public void testContainsAnyOf() 
	{
		ORefList orefList1 = new ORefList();
		ORefList orefList2 = new ORefList();
		assertFalse("contains refs from other list?", orefList1.containsAnyOf(orefList2));
		assertFalse("contains refs from other list?", orefList2.containsAnyOf(orefList1));
		
		orefList2.add(new ORef(Indicator.getObjectType(), new BaseId(4)));
		orefList2.add(new ORef(Indicator.getObjectType(), new BaseId(5)));
		assertFalse("contains refs from other list?", orefList1.containsAnyOf(orefList2));
		
		orefList1.add(new ORef(Indicator.getObjectType(), new BaseId(5)));
		assertTrue("does not contain ref from other list?", orefList1.containsAnyOf(orefList2));
	}
	
	public void testGetOverlappingRefs()
	{
		ORefList orefList1 = new ORefList();
		orefList1.add(new ORef(Indicator.getObjectType(), new BaseId(5)));
		
		ORefList orefList2 = new ORefList();
		assertEquals("has overlapping refs?", 0, orefList1.getOverlappingRefs(orefList2).size());
		
		orefList2.add(new ORef(Indicator.getObjectType(), new BaseId(4)));
		orefList2.add(new ORef(Indicator.getObjectType(), new BaseId(5)));
		assertEquals("has overlapping refs?", 1, orefList1.getOverlappingRefs(orefList2).size());
	}
}