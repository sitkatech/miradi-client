/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogfields.editors;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Timeframe;
import org.miradi.project.Project;
import org.miradi.questions.AbstractDateUnitTypeQuestion;
import org.miradi.questions.PlannedDateUnitTypeQuestion;
import org.miradi.schemas.TimeframeSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

import java.util.Set;

public class TimeframeEditorComponent extends WhenEditorComponent
{
	public TimeframeEditorComponent(Project project, BaseObject baseObjectToUse) throws Exception
	{
		this(project, baseObjectToUse.getTimeframeRefs());
	}

	public TimeframeEditorComponent(Project project, ORefList planningObjectRefs) throws Exception
	{
		super(project, planningObjectRefs, new PlannedDateUnitTypeQuestion(project, planningObjectRefs));
	}

	@Override
	protected String getPanelTitle()
	{
		return "";
	}

	@Override
	public DateUnitEffortList getDateUnitEffortList(ORef planningObjectRef) throws Exception
	{
		Timeframe timeframe = Timeframe.find(getProject(), planningObjectRef);
		return timeframe.getDateUnitEffortList();
	}

	@Override
	protected TimePeriodCostsMap getTimePeriodCostsMap(ORef planningObjectRef) throws Exception
	{
		Timeframe timeframe = Timeframe.find(getProject(), planningObjectRef);
		return timeframe.getTimeframesTimePeriodCostsMap();
	}

	@Override
	protected String getDefaultDateUnitTypeCode(ORefList planningObjectRefsToUse) throws Exception
	{
		if (planningObjectRefsToUse.isEmpty())
			return AbstractDateUnitTypeQuestion.NONE_CODE;

		ORef planningObjectRef = planningObjectRefsToUse.getFirstElement();
		Timeframe timeframe = Timeframe.find(getProject(), planningObjectRef);
		TimePeriodCostsMap timePeriodCostsMap = timeframe.convertAllDateUnitEffortList();

		Set<DateUnit> dateUnits = timePeriodCostsMap.getDateUnits();
		return getDateUnitCode(dateUnits);
	}

	public static void setTimeframeValue(Project project, BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		project.executeBeginTransaction();
		try
		{
			clearTimeframeDateUnitEfforts(project, baseObjectForRow);

			ORefList timeframeRefs = baseObjectForRow.getTimeframeRefs();

			if (datesAsCodeList.hasData() && timeframeRefs.isEmpty())
				createTimeframe(project, baseObjectForRow, datesAsCodeList);

			if (datesAsCodeList.hasData() && timeframeRefs.hasRefs())
				updateTimeframe(project, timeframeRefs, datesAsCodeList);

			if (datesAsCodeList.isEmpty() && timeframeRefs.size() == 1)
				deleteEmptyTimeframe(project, timeframeRefs.getFirstElement());
		}
		finally
		{
			project.executeEndTransaction();
		}
	}

	private static void updateTimeframe(Project project, ORefList timeframeRefs, CodeList datesAsCodeList) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		for (int index = 0; index < timeframeRefs.size(); ++index)
		{
			setTimeframeDateUnitEffortList(project, timeframeRefs.get(index), dateUnitEffortList);
		}
	}

	private static void clearTimeframeDateUnitEfforts(Project project, BaseObject baseObjectForRow) throws Exception
	{
		ORefList timeframeRefs = baseObjectForRow.getTimeframeRefs();
		for (int index = 0; index < timeframeRefs.size(); ++index)
		{
			Timeframe timeframe = Timeframe.find(project, timeframeRefs.get(index));
			Command clearDateUnitEffortList = new CommandSetObjectData(timeframe, ResourceAssignment.TAG_DATEUNIT_DETAILS, new DateUnitEffortList().toString());
			project.executeCommand(clearDateUnitEffortList);
		}
	}

	private static void deleteEmptyTimeframe(Project project, ORef timeframeRef) throws Exception
	{
		Timeframe timeframe = Timeframe.find(project, timeframeRef);
		if (timeframe.isEmpty())
		{
			CommandVector removeTimeframeCommands = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(project, timeframe, BaseObject.TAG_TIMEFRAME_IDS);
			project.executeCommands(removeTimeframeCommands);
		}
	}

	private static void createTimeframe(Project project, BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		CommandCreateObject createTimeframe = new CommandCreateObject(TimeframeSchema.getObjectType());
		project.executeCommand(createTimeframe);

		ORef timeframeRef = createTimeframe.getObjectRef();
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		setTimeframeDateUnitEffortList(project, timeframeRef, dateUnitEffortList);

		CommandSetObjectData appendTimeframe = CommandSetObjectData.createAppendIdCommand(baseObjectForRow, BaseObject.TAG_TIMEFRAME_IDS, timeframeRef);
		project.executeCommand(appendTimeframe);
	}

	private static void setTimeframeDateUnitEffortList(Project project, ORef timeframeRef, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		CommandSetObjectData addEffortList = new CommandSetObjectData(timeframeRef, Timeframe.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
		project.executeCommand(addEffortList);
	}
}