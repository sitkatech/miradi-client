/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.objects;

import java.awt.Point;

import org.martus.util.MultiCalendar;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.ids.TaskId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objectdata.BaseIdData;
import org.miradi.objectdata.BooleanData;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.CodeListData;
import org.miradi.objectdata.DateData;
import org.miradi.objectdata.DateRangeData;
import org.miradi.objectdata.DateRangeEffortListData;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.IntegerData;
import org.miradi.objectdata.NumberData;
import org.miradi.objectdata.ORefData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objectdata.PointListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.RelevancyOverrideSetData;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objects.BaseObject.PseudoQuestionData;
import org.miradi.objects.BaseObject.PseudoStringData;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.InternalQuestionWithoutValues;
import org.miradi.questions.QuestionManager;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;
import org.miradi.utils.StringMapData;

public class ObjectTestCase extends TestCaseWithProject
{
	public ObjectTestCase(String name)
	{
		super(name);
	}

	public void verifyFields(int objectType) throws Exception
	{
		verifyObjectCount(objectType);
		verifyFields(objectType, null);
	}
	
	private void verifyObjectCount(int objectType)
	{
		boolean isLessThanObjectTypeCount = objectType < ObjectType.OBJECT_TYPE_COUNT;
		assertTrue("object id not less than count", isLessThanObjectTypeCount);
	}

	public void verifyFields(int objectType, CreateObjectParameter extraInfo) throws Exception
	{
		Project project = createAndOpenProject();
		try
		{
			BaseId id = project.createObject(objectType, BaseId.INVALID, extraInfo);
			BaseObject object = project.findObject(objectType, id);
			String[] tags = object.getFieldTags();
			for(int i = 0; i < tags.length; ++i)
			{
				if(object.isPseudoField(tags[i]))
					continue;
				
				verifyShortLabelField(object, tags[i]);
				verifyFieldLifecycle(project, object, tags[i]);
			}
		}
		finally
		{
			project.close();
		}
		
		verifyLoadPool(objectType, extraInfo);
	}

	public ProjectForTesting createAndOpenProject() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		return project;
	}
	 
	private void verifyLoadPool(int objectType, CreateObjectParameter extraInfo) throws Exception
	{
		ProjectForTesting project= createAndOpenProject();
		try
		{
			BaseId id = BaseId.INVALID;
			id = project.createObject(objectType, BaseId.INVALID, extraInfo);
			project.closeAndReopen();
			BaseObject object = project.findObject(objectType, id);
			assertNotNull("Didn't load pool?", object);
		}
		finally
		{
			project.close();
		}
	}
	
	private void verifyFieldLifecycle(Project project, BaseObject object, String tag) throws Exception
	{
		if(tag.equals(BaseObject.TAG_ID))
			return;
		
		if (object.getNoneClearedFieldTags().contains(tag))
			return;
				
		if (object.isPseudoField(tag))
			return;
		
		String sampleData = getSampleData(object, tag);
		String emptyData = getEmptyData(object, tag);

		assertEquals("didn't default " + tag + " empty?", emptyData, object.getData(tag));
		try
		{
			object.setData(tag, sampleData);	
		}
		catch(Exception e)
		{
			System.out.println("need sample data for " + object.getField(tag).getClass().getSimpleName());
			throw e;
		}
		
		assertEquals("did't set " + tag + "?", sampleData, object.getData(tag));
		BaseObject got = BaseObject.createFromJson(project.getObjectManager(), object.getType(), object.toJson());
		assertEquals("didn't jsonize " + tag + "?", object.getData(tag), got.getData(tag));
		
		CommandSetObjectData[] commandsToDelete = object.createCommandsToClear();
		for(int i = 0; i < commandsToDelete.length; ++i)
		{
			assertNotEquals("Tried to clear Id?", BaseObject.TAG_ID, commandsToDelete[i].getFieldTag());
			project.executeCommand(commandsToDelete[i]);
		}
		assertEquals("Didn't clear " + tag + "?", emptyData, object.getData(tag));
		project.undo();
		assertEquals("Didn't restore " + tag + "?", sampleData, object.getData(tag));
	}

	private void verifyShortLabelField(BaseObject object, String tag) throws Exception
	{
		if (!tag.equals("ShortLabel"))
			return;
		
		object.setData(tag, "someShortLabelValue");
		assertEquals("didnt return correct value for field " + tag + ":?", "someShortLabelValue", object.getShortLabel());
		
		object.setData(tag, "");
	}

	private String getEmptyData(BaseObject object, String tag)
	{
		ObjectData field = object.getField(tag);
		if(field instanceof DateRangeEffortListData)
			return new DateRangeEffortList().toString();
		
		if(field instanceof ORefData)
			return ORef.INVALID.toString();
		
		if (field instanceof IntegerData)
			return new IntegerData("tag").toString();
		
		return "";
	}
	
	private String getSampleData(BaseObject object, String tag) throws Exception
	{
		ObjectData field = object.getField(tag);
		if(field instanceof IdListData)
		{
			IdList list = new IdList(0);
			list.add(new BaseId(7));
			return list.toString();
		}
		else if(field instanceof StringMapData)
		{
			StringMap list = new StringMap();
			list.add("A","RolaA");
			return list.toString();
		}
		else if(field instanceof ChoiceData)
		{
			return "3";
		}
		else if(field instanceof BaseIdData)
		{
			return new BaseId(15).toString();
		}
		else if(field instanceof DateData)
		{
			return MultiCalendar.createFromGregorianYearMonthDay(1953, 10, 21).toString();
		}
		else if (field instanceof PointListData)
		{
			PointListData pointList = new PointListData("tag");
			pointList.add(new Point(-1, 55));
			
			return pointList.toString();
		}
		else if(field instanceof DateRangeEffortListData)
		{
			DateRangeEffortList list = new DateRangeEffortList();
			MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(2007, 03, 29);
			MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(2007, 03, 30);
			DateRange range = new DateRange(startDate, endDate);
			list.add(new DateRangeEffort("hours", 5.0, range));
			return list.toString();
		}
		else if(field instanceof StringData)
		{
			return tag + tag;
		}
		else if(field instanceof PseudoQuestionData)
		{
			return "";
		}
		else if(field instanceof PseudoStringData)
		{
			return "";
		}
		else if(field instanceof ORefData)
		{
			return new ORef(ObjectType.TASK, new TaskId(283)).toString();
		}
		else if(field instanceof ORefListData)
		{
			ORef test = new ORef(ObjectType.TASK, new TaskId(283));
			ORefList list = new ORefList(new ORef[] {test});
			ORefListData listData = new ORefListData("tag");
			listData.set(list.toString());
			return listData.toString();
		}
		else if(field instanceof CodeListData)
		{
			CodeListData codeList = new CodeListData("tag", QuestionManager.getQuestion(InternalQuestionWithoutValues.class));
			codeList.add("A1");
			codeList.add("B1");
			return codeList.toString();
			
		}
		else if (field instanceof BooleanData)
		{
			return "1";
		}
		else if (field instanceof IntegerData)
		{
			return "3";
		}
		else if (field instanceof RelevancyOverrideSetData)
		{
			RelevancyOverride test = new RelevancyOverride(new ORef(Cause.getObjectType(), new BaseId(44)), true);
			RelevancyOverrideSet overrideSet = new RelevancyOverrideSet();
			overrideSet.add(test);
			
			RelevancyOverrideSetData overrideSetData = new RelevancyOverrideSetData("tag");
			overrideSetData.set(overrideSet.toString());
			return overrideSetData.toString();
		}
		else if (field instanceof NumberData)
		{
			return "27.65";
		}
		else if (field instanceof DateRangeData)
		{
			MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(2007, 7, 7);
			MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(2008, 8, 8);
			DateRange dateRange = new DateRange(startDate, endDate);
			
			DateRangeData dateRangeData = new DateRangeData("");
			dateRangeData.set(dateRange.toJson().toString());
			
			return dateRangeData.toString();
		}
		else
		{
			throw new RuntimeException("Need to add sample data for " + object.getType() + ":" + tag + " type: " + field.getClass().getSimpleName());
		}
	}
}
