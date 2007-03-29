/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class TestObjectDirectOwner extends EAMTestCase
{
	public TestObjectDirectOwner(String name)
	{
		super(name);
	}

	public void testObjectDirectOwner()
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
	
	class Owner extends Target
	{

		public Owner(FactorId idToUse)
		{
			super(idToUse);
			// TODO Auto-generated constructor stub
		}
	}

}
