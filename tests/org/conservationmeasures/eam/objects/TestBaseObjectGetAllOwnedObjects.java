/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestBaseObjectGetAllOwnedObjects extends EAMTestCase
{
	public TestBaseObjectGetAllOwnedObjects(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}

	public void testGetAllOwnedObjects() throws Exception
	{
		BaseId factorId = project.createFactorAndReturnId(ObjectType.CAUSE);
		project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		project.addItemToFactorList(factorId, ObjectType.OBJECTIVE, Factor.TAG_OBJECTIVE_IDS);
		
	   	ORef ownerRef = new ORef(ObjectType.THREAT_REDUCTION_RESULT, factorId);
	   	BaseObject ownerObject = project.findObject(ownerRef);	
	   	ORefList allOwnedObjects = ownerObject.getAllOwnedObjects();
	   	assertEquals("incorrect owned object count?", 2, allOwnedObjects.size());
	}

	ProjectForTesting project;
}
