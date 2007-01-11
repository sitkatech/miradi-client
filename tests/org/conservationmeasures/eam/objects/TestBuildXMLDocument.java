/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.io.File;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objectdata.ObjectData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.project.Project;
import org.martus.util.xml.XmlUtilities;

public class TestBuildXMLDocument extends EAMTestCase
{
	public TestBuildXMLDocument(String name)
	{
		super(name);
	}

	public void testBuild() throws Exception
	{
		String projectName = "exportedProject";
		try
		{
			File projectFile = new File(EAM.getHomeDirectory(),projectName);
			Project project = new Project();
			project.createOrOpen(projectFile);
			
			//processObjectPool(project, "Fake",ObjectType.FAKE);
			
			System.out.print("<?xml version=\"1.0\" encoding=\"US-ASCII\"?>");
			
			System.out.print("<Mardis" +  " project=\"" + projectName + "\">");
			
			processObjectPool(project, "AccountingCode", 	ObjectType.ACCOUNTING_CODE);
			processObjectPool(project, "Assignment", 		ObjectType.ASSIGNMENT);
			processObjectPool(project, "DiagramLink",		ObjectType.DIAGRAM_LINK);
			processObjectPool(project, "Factor", 			ObjectType.FACTOR);
			processObjectPool(project, "FactorLink",		ObjectType.FACTOR_LINK);
			processObjectPool(project, "FundingSource",		ObjectType.FUNDING_SOURCE);
			processObjectPool(project, "Goal", 				ObjectType.GOAL);
			processObjectPool(project, "Indicator", 		ObjectType.INDICATOR);
			processObjectPool(project, "Objective", 		ObjectType.OBJECTIVE);
			processObjectPool(project, "ProjectMetaData", 	ObjectType.PROJECT_METADATA);
			processObjectPool(project, "ProjectResource", 	ObjectType.PROJECT_RESOURCE);
			processObjectPool(project, "RatingCriterion", 	ObjectType.RATING_CRITERION);
			processObjectPool(project, "Task", 				ObjectType.TASK);
			processObjectPool(project, "ValueOption", 		ObjectType.VALUE_OPTION);
			processObjectPool(project, "ViewData", 			ObjectType.VIEW_DATA);
			
			System.out.print("</Mardis>");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void processObjectPool(Project project ,String elementName, int objectType) throws Exception
	{
			EAMObjectPool pool = project.getPool(objectType);
			if (pool==null)
			{
				throw new Exception("POOL NOT FOUND:" + objectType);
			}
			
			BaseId[] baseIds = pool.getIds();
			for(int i = 0; i < baseIds.length; ++i)
			{
				EAMBaseObject object = (EAMBaseObject) project.findObject(objectType, baseIds[i]);
				
				System.out.print("<"+elementName+  "  id=\"" + i + "\">");
				processTags(object);
				System.out.print("</"+elementName+">");
			}
	}

	private void processTags(EAMBaseObject object)
	{
		String[] tags = object.getFieldTags();
		for(int i = 0; i < tags.length; ++i)
		{
			
			System.out.print("<"+tags[i]+">");
			ObjectData field = object.getField(tags[i]);
			if (tags[i].endsWith("Ids"))
				buildFieldIDListElements(field);
			else
				System.out.print(XmlUtilities.getXmlEncoded(field.get()));
			
			System.out.print("</"+tags[i]+">");
		}
	}

	private void buildFieldIDListElements(ObjectData field)
	{
		try 
		{
			IdList idList = new IdList(field.get());
			for (int i=0; i<idList.size(); ++i )
			{
				System.out.print("<ref idref=\"" + idList.get(i) + "\"/>");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}

