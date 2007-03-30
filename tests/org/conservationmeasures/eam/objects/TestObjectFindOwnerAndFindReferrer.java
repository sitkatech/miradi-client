/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.io.File;
import java.io.IOException;

import org.conservationmeasures.eam.ids.BaseId;
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
		loadAndOpenTestProject();
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	

	public void loadAndOpenTestProject() throws Exception
	{
		File zip = new File("./Marine Example");
		project.createOrOpen(zip);
	}

	//TODO: not hooked in to main test as it is still in process of being wrttien
	public void testTwoElementOneQouted() throws IOException
	{
		//EAMObjectPool pool = project.getPool(ObjectType.INDICATOR);
		//System.out.println(pool.getIdList());
		BaseObject indicatorFound = project.findObject(ObjectType.INDICATOR, new BaseId(62));
		assertNotNull(indicatorFound);
		assertEquals("Percent of boats with rat control barriers in harbor", indicatorFound.getLabel());
		ORef oref = indicatorFound.findObjectWhoOwnesUs(project, ObjectType.FACTOR, indicatorFound.getRef());
		assertNotNull(oref);
		BaseObject factorFound = project.findObject(oref);
		assertEquals("Re-introduction of Rats", factorFound.getLabel());
	}
	
	ProjectForTesting project;
}
