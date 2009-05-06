/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.objects;

import java.io.File;
import java.io.PrintStream;

import org.martus.util.DirectoryUtils;
import org.martus.util.xml.XmlUtilities;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.EAMXmlFileChooser;

public class TestBuildXMLDocument extends EAMTestCase
{
	public TestBuildXMLDocument(String name)
	{
		super(name);
	}

	public void testBuild() throws Exception
	{
		String projectName = "Marine demo 06-10-20";
		
		EAMFileSaveChooser eamFileChooser = new EAMXmlFileChooser(MainWindow.create());
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) return;
		System.setOut(new PrintStream(chosen));
		
		File tempDirectory = createTempDirectory();
		try
		{
			Project project = new Project();
			project.setLocalDataLocation(tempDirectory);
			project.createOrOpen(projectName);
			
			//processObjectPool(project, "Fake",ObjectType.FAKE);
			
			writeXMLVersionLine();
			writeLineReturn();
			writeStartElementWithNamedAttr("Miradi", "project", projectName);

			
			processObjectPool(project, "AccountingCodes" , "AccountingCode", ObjectType.ACCOUNTING_CODE);
			processObjectPool(project, "Assignments", "Assignment", ObjectType.RESOURCE_ASSIGNMENT);
			processObjectPool(project, "DiagramLinks", "DiagramLink",ObjectType.DIAGRAM_LINK);
			
			processFactorObjectPool(project, "Strategys", "Strategy", ObjectType.STRATEGY);
			processFactorObjectPool(project, "DraftStrategys", "Strategy", ObjectType.STRATEGY);
			processFactorObjectPool(project, "Targets", "Target", ObjectType.TARGET);
			processFactorObjectPool(project, "ContributingFactors", "ContributingFactor", ObjectType.CAUSE);
			processFactorObjectPool(project, "Causes", "Cause", ObjectType.CAUSE);

			processObjectPool(project, "FactorLinks", "FactorLink",	ObjectType.FACTOR_LINK);
			processObjectPool(project, "FundingSources", "FundingSource", ObjectType.FUNDING_SOURCE);
			processObjectPool(project, "Goals", "Goal", ObjectType.GOAL);
			processObjectPool(project, "Indicators", "Indicator", ObjectType.INDICATOR);
			processObjectPool(project, "Objectives", "Objective", ObjectType.OBJECTIVE);
			processObjectPool(project, "ProjectMeta", "ProjectMetaData", ObjectType.PROJECT_METADATA);
			processObjectPool(project, "ProjectResources", "ProjectResource", ObjectType.PROJECT_RESOURCE);
			processObjectPool(project, "RatingCriterions", "RatingCriterion", ObjectType.RATING_CRITERION);
			processObjectPool(project, "Tasks", "Task", ObjectType.TASK);
			processObjectPool(project, "ValueOptions", "ValueOption", ObjectType.VALUE_OPTION);
			processObjectPool(project, "Views", "ViewData", ObjectType.VIEW_DATA);
			
			writeLineReturn();
			writeEndELement("Miradi");
			
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}

	

	private void processFactorObjectPool(Project project, String groupElementName ,String elementName, int objectType) throws Exception
	{
		writeLineReturn();
		writeStartELement(groupElementName);

		EAMObjectPool pool = project.getPool(objectType);
		BaseId[] baseIds = pool.getIds();
		for(int i = 0; i < baseIds.length; ++i)
		{
			Factor object = (Factor) project.findObject(objectType, baseIds[i]);
			if (!object.isContributingFactor() && groupElementName.equals("ContributingFactors"))
				continue;
			else if (!object.isTarget() && groupElementName.equals("Targets"))
				continue;
			else if (!object.isStatusDraft() && groupElementName.equals("DraftStrategys"))
				continue;
			else if (!object.isStrategy() && groupElementName.equals("Strategys"))
				continue;
			else if (!object.isCause() && groupElementName.equals("Causes"))
				continue;

			processTags(elementName, baseIds, i, object);
		}
			
		writeLineReturn();
		writeEndELement(groupElementName);
	}
	
	private void processObjectPool(Project project, String GroupElementName ,String elementName, int objectType) throws Exception
	{
		writeLineReturn();
		writeStartELement(GroupElementName);

		EAMObjectPool pool = project.getPool(objectType);
		BaseId[] baseIds = pool.getIds();
		for(int i = 0; i < baseIds.length; ++i)
		{
			BaseObject object = project.findObject(objectType, baseIds[i]);
			processTags(elementName, baseIds, i, object);
		}
			
		writeLineReturn();
		writeEndELement(GroupElementName);
	}

	private void processTags(String elementName, BaseId[] baseIds, int i, BaseObject object)
	{
		writeLineReturn();
		writeStartELement(elementName, baseIds[i].asInt());
		processTags(object);
		writeLineReturn();
		writeEndELement(elementName);
	}

	private void processTags(BaseObject object)
	{
		
		if (object.getType() == ObjectType.TASK)
		{
			BaseId parentRefId = ((Task)object).getOwnerRef().getObjectId();
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
				buildFieldIDListElements((IdListData)field);
			else
				writeData(field.get());
			
			writeEndELement(tags[i]);
		}

	}


	private void buildFieldIDListElements(IdListData field)
	{
		try 
		{
			IdList idList = new IdList(field.getIdList().getObjectType(), field.get());
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
			EAM.logException(e);
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

