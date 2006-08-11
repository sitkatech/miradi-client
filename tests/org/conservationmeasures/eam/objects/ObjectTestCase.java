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
			EAMObject object = project.findObject(objectType, id);
			assertEquals("didn't default " + tag + " blank?", "", object.getData(tag));
			object.setData(tag, sampleData);
			assertEquals("did't set " + tag + "?", sampleData, object.getData(tag));
			EAMObject got = EAMBaseObject.createFromJson(objectType, object.toJson());
			assertEquals("didn't jsonize " + tag + "?", object.getData(tag), got.getData(tag));
		}
		finally
		{
			project.close();
		}
	}
	
	public void verifyIdListField(int objectType, String tag) throws Exception
	{
		Project project = new ProjectForTesting(getName());
		try
		{
			IdList sampleData = new IdList();
			sampleData.add(7);
			sampleData.add(49);
			int id = project.createObject(objectType);
			EAMObject object = project.findObject(objectType, id);
			assertEquals("didn't default " + tag + " empty?", 0, new IdList(object.getData(tag)).size());
			object.setData(tag, sampleData.toString());
			assertEquals("did't set " + tag + "?", sampleData, new IdList(object.getData(tag)));
			EAMObject got = EAMBaseObject.createFromJson(objectType, object.toJson());
			assertEquals("didn't jsonize " + tag + "?", object.getData(tag), got.getData(tag));
		}
		finally
		{
			project.close();
		}
	}
}
