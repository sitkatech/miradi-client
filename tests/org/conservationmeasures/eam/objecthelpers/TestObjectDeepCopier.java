/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestObjectDeepCopier extends EAMTestCase
{
	public TestObjectDeepCopier(String name)
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

	public void testDeepCopy() throws Exception
	{
		BaseId indicatorId = project.createObject(ObjectType.INDICATOR);
		IdList indicatorIds = new IdList();
		indicatorIds.add(indicatorId);
	
		BaseId causeId = project.createFactor(ObjectType.CAUSE);
		Cause cause = (Cause) project.findObject(new ORef(Cause.getObjectType(), causeId));
		cause.setIndicators(indicatorIds);
		
		ObjectDeepCopier deepCopier = new ObjectDeepCopier(project);
		Vector deepCopiedFactor = deepCopier.createDeepCopy(cause);		
		assertEquals("not all objects copied?", 2, deepCopiedFactor.size());
	}
	
	ProjectForTesting project;
}
