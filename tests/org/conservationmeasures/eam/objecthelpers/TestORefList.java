/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
		ORefList goalList = objRefList.extractByType(ObjectType.GOAL);
		assertTrue(goalList.size()==2);
		assertTrue(goalList.contains(objectRef1));
		assertTrue(goalList.contains(objectRef2));
		assertFalse(goalList.contains(objectRef3));
	}
}