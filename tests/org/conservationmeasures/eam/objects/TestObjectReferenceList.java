/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectReference;
import org.conservationmeasures.eam.objecthelpers.ObjectReferenceList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class TestObjectReferenceList extends EAMTestCase
{
	public TestObjectReferenceList(String name)
	{
		super(name);
	}

	public void testObjectReferenceList()
	{
		ObjectReferenceList objRefList = new ObjectReferenceList();
		ObjectReference objectRef = new ObjectReference(ObjectType.GOAL, BaseId.INVALID);
		objRefList.add(objectRef);
		EnhancedJsonObject json = objRefList.toJson();
		ObjectReferenceList got = new ObjectReferenceList(json);
		assertEquals("lists are equal?", objRefList, got);
		
		ObjectReference gotObjRef = objRefList.get(0);
		assertEquals("object references equal?", objectRef, gotObjRef);
	}
}