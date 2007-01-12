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
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.project.Project;

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
			
			writeXMLVersionLine();
			
			System.out.print("<Mardis" +  " project=\"" + projectName + "\">");
			
			processObjectPool(project, AccountingCode.OBJECT_NAME, 	ObjectType.ACCOUNTING_CODE);
			processObjectPool(project, Assignment.OBJECT_NAME, 		ObjectType.ASSIGNMENT);
			processObjectPool(project, "DiagramLink",				ObjectType.DIAGRAM_LINK);
			processObjectPool(project, Factor.OBJECT_NAME, 			ObjectType.FACTOR);
			processObjectPool(project, "FactorLink",				ObjectType.FACTOR_LINK);
			processObjectPool(project, "FundingSource",				ObjectType.FUNDING_SOURCE);
			processObjectPool(project, Goal.OBJECT_NAME, 			ObjectType.GOAL);
			processObjectPool(project, Indicator.OBJECT_NAME, 		ObjectType.INDICATOR);
			processObjectPool(project, Objective.OBJECT_NAME, 		ObjectType.OBJECTIVE);
			processObjectPool(project, "ProjectMetaData", 	ObjectType.PROJECT_METADATA);
			processObjectPool(project, "ProjectResource", 	ObjectType.PROJECT_RESOURCE);
			processObjectPool(project, "RatingCriterion", 	ObjectType.RATING_CRITERION);
			processObjectPool(project, "Task", 				ObjectType.TASK);
			processObjectPool(project, "ValueOption", 		ObjectType.VALUE_OPTION);
			processObjectPool(project, "ViewData", 			ObjectType.VIEW_DATA);
			
			writeEndELement("Mardis");
			
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
				
				writeStartELementWithIDRef(elementName,i);
				processTags(object);
				writeEndELement(elementName);
			}
	}

	private void processTags(EAMBaseObject object)
	{
		String[] tags = object.getFieldTags();
		for(int i = 0; i < tags.length; ++i)
		{
			
			writeStartELement(tags[i]);
			

			ORef oref = object.getRef();
			System.out.println(oref.getObjectType()); // need a translation method
			System.out.println(oref.getObjectId());
			
			CreateObjectParameter cop = object.getCreationExtraInfo();
			if (cop != null)
				System.out.println(cop.toString());
			
// ARE we getting everything what is in extra info 			
			
			ObjectData field = object.getField(tags[i]);
			if (tags[i].endsWith("Ids"))
				buildFieldIDListElements(field);
			else
				writeData(field.get());
			
			writeEndELement(tags[i]);
		}
	}

	
	private void buildFieldIDListElements(ObjectData field)
	{
		try 
		{
			IdList idList = new IdList(field.get());
			for (int i=0; i<idList.size(); ++i )
			{
				writeIDRefElement(idList.get(i).asInt());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	private void writeStartELement(String name)
	{
		//System.out.print("<"+name+">");
	}
	
	private void writeStartELementWithIDRef(String name, int id)
	{
		//System.out.print("<"+name+  "  id=\"" + id + "\">");
	}
	
	private void writeIDRefElement(int id)
	{
		//System.out.print("<ref idref=\"" + id + "\"/>");
	}
	
	private void writeEndELement(String name)
	{
		//System.out.print("</"+name+">");
	}
	
	private void writeData(String text)
	{
		//System.out.print(XmlUtilities.getXmlEncoded(text));
	}
	
	private void writeXMLVersionLine()
	{
		//System.out.print("<?xml version=\"1.0\" encoding=\"US-ASCII\"?>");
	}
	
}

