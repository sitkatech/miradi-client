/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestObjectFindOwnerAndFindReferrer extends EAMTestCase
{
	public TestObjectFindOwnerAndFindReferrer(String name)
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


	//TODO: not hooked in to main test as it is still in process of being wrttien
	public void testTwoElementOneQouted() throws Exception
	{
		BaseId factorId = project.createFactor(Factor.TYPE_TARGET);
		BaseId indicatorId = project.createObject(ObjectType.INDICATOR);
		IdList indicatorList = new IdList(new BaseId[] {indicatorId});
		project.setObjectData(ObjectType.FACTOR, factorId, Factor.TAG_INDICATOR_IDS, indicatorList.toString());
		BaseObject indicatorFound = project.findObject(ObjectType.INDICATOR, indicatorId);

		ORef oref = indicatorFound.findObjectWhoOwnesUs(project, ObjectType.FACTOR, indicatorFound.getRef());
		assertNotNull(oref);
		BaseObject factorFound = project.findObject(oref);
		assertEquals(factorId, factorFound);
	}
	
	ProjectForTesting project;
}
