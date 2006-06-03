/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class ObjectTestCase extends EAMTestCase
{
	public ObjectTestCase(String name)
	{
		super(name);
	}

	public void verifyTextField(int objectType, String tag) throws Exception
	{
		Project project = new ProjectForTesting(getName());
		try
		{
			final String sampleData = "Blah blah";
			int id = project.createObject(objectType);
			EAMObject objective = project.findObject(objectType, id);
			assertEquals("didn't default " + tag + " blank?", "", objective.getData(tag));
			objective.setData(tag, sampleData);
			assertEquals("did't set " + tag + "?", sampleData, objective.getData(tag));
			Objective got = new Objective(objective.toJson());
			assertEquals("didn't jsonize " + tag + "?", objective.getData(tag), got.getData(tag));
		}
		finally
		{
			project.close();
		}
	
	}

}
