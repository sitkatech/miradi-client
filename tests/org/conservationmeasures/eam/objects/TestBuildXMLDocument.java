/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.io.File;
import java.io.PrintStream;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objectdata.ObjectData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMFileSaveChooser;
import org.conservationmeasures.eam.utils.EAMXmlFileChooser;
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
		
		EAMFileSaveChooser eamFileChooser = new EAMXmlFileChooser(new MainWindow());
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) return;
		System.setOut(new PrintStream(chosen));
		
		try
		{
			File projectFile = new File(EAM.getHomeDirectory(),projectName);
			Project project = new Project();
			project.createOrOpen(projectFile);
			
			//processObjectPool(project, "Fake",ObjectType.FAKE);
			
			writeXMLVersionLine();
			writeLineReturn();
			writeStartElementWithNamedAttr("Miradi", "project", projectName);

			
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
			
			writeLineReturn();
			writeEndELement("Miradi");
			
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
				writeLineReturn();
				writeStartELement(elementName, baseIds[i].asInt());
				processTags(object);
				writeLineReturn();
				writeEndELement(elementName);
			}
	}

	private void processTags(EAMBaseObject object)
	{
		
		if (object.getType() == ObjectType.TASK)
		{
			BaseId parentRefId = ((Task)object).getParentRef().getObjectId();
			if (parentRefId!=null)
			{
				int parentRef = parentRefId.asInt();
				writeLineReturn();
				writeLineTab();
				writeParentRefElement(parentRef);
			}
		}
			
		String[] tags = object.getFieldTags();
		for(int i = 0; i < tags.length; ++i)
		{
			writeLineReturn();
			writeLineTab();
			writeStartELement(tags[i]);
			
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
				writeLineReturn();
				writeLineTab();
				writeLineTab();
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
		write("<"+name+">");
	}
	
	private void writeStartELement(String name, int id)
	{
		write("<"+name  +  "  id=\"" + id  + "\">");
	}
	
	private void writeIDRefElement(int id)
	{
		write("<ref idref=\"" + + id + "\"/>");
	}
	
	private void writeParentRefElement(int id)
	{
		write("<parentref idref=\"" + + id + "\"/>");
	}
	
	private void writeEndELement(String name)
	{
		write("</"+name+">");
	}
	
	private void writeData(String text)
	{
		write(XmlUtilities.getXmlEncoded(text));
	}
	
	private void writeStartElementWithNamedAttr(String name, String attrName, String attrValue)
	{
		write("<"+name  +  "  " + attrName + "=\"" + attrValue + "\">");
	}
	
	private void writeXMLVersionLine()
	{
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	}
	
	private void write(String text)
	{
		System.out.print(text);
	}
	
	private void writeLineReturn()
	{
		System.out.println();
	}
	
	private void writeLineTab()
	{
		System.out.print("       ");
	}
}

