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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ResourcePlan;
import org.miradi.project.Project;
import org.miradi.questions.PlannedDateUnitTypeQuestion;
import org.miradi.schemas.ResourcePlanSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

public class WhenPlannedEditorComponent extends WhenEditorComponent
{
	public WhenPlannedEditorComponent(Project project, BaseObject baseObjectToUse) throws Exception
	{
		this(project, baseObjectToUse.getResourcePlanRefs());
	}

	public WhenPlannedEditorComponent(Project project, ORefList planningObjectRefs) throws Exception
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
		ResourcePlan resourcePlan = ResourcePlan.find(getProject(), planningObjectRef);
		return resourcePlan.getDateUnitEffortList();
	}

	@Override
	protected TimePeriodCostsMap getTimePeriodCostsMap(ORef planningObjectRef) throws Exception
	{
		ResourcePlan resourcePlan = ResourcePlan.find(getProject(), planningObjectRef);
		return resourcePlan.getResourcePlansTimePeriodCostsMap();
	}

	public static void setWhenPlannedValue(Project project, BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		project.executeBeginTransaction();
		try
		{
			clearResourcePlanDateUnitEfforts(project, baseObjectForRow);
			ORefList resourcePlanRefs = baseObjectForRow.getResourcePlanRefs();
			if (datesAsCodeList.hasData() && resourcePlanRefs.isEmpty())
				createResourcePlan(project, baseObjectForRow, datesAsCodeList);

			if (datesAsCodeList.hasData() && resourcePlanRefs.hasRefs())
				updateResourcePlans(project, resourcePlanRefs, datesAsCodeList);

			if (datesAsCodeList.isEmpty() && resourcePlanRefs.size() == 1)
				deleteEmptyResourcePlan(project, resourcePlanRefs.getFirstElement());
		}
		finally
		{
			project.executeEndTransaction();
		}
	}

	private static void updateResourcePlans(Project project, ORefList resourcePlanRefs, CodeList datesAsCodeList) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		for (int index = 0; index < resourcePlanRefs.size(); ++index)
		{
			setResourcePlanDateUnitEffortList(project, resourcePlanRefs.get(index), dateUnitEffortList);
		}
	}

	private static void clearResourcePlanDateUnitEfforts(Project project, BaseObject baseObjectForRow) throws Exception
	{
		ORefList resourcePlanRefs = baseObjectForRow.getResourcePlanRefs();
		for (int index = 0; index < resourcePlanRefs.size(); ++index)
		{
			ResourcePlan resourcePlan = ResourcePlan.find(project, resourcePlanRefs.get(index));
			Command clearDateUnitEffortList = new CommandSetObjectData(resourcePlan, ResourceAssignment.TAG_DATEUNIT_EFFORTS, new DateUnitEffortList().toString());
			project.executeCommand(clearDateUnitEffortList);
		}
	}

	private static void deleteEmptyResourcePlan(Project project, ORef resourcePlanRef) throws Exception
	{
		ResourcePlan resourcePlan = ResourcePlan.find(project, resourcePlanRef);
		if (resourcePlan.isEmpty())
		{
			CommandVector removePlanCommands = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(project, resourcePlan, BaseObject.TAG_RESOURCE_PLAN_IDS);
			project.executeCommands(removePlanCommands);
		}
	}

	private static void createResourcePlan(Project project, BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		CommandCreateObject createResourcePlan = new CommandCreateObject(ResourcePlanSchema.getObjectType());
		project.executeCommand(createResourcePlan);

		ORef resourcePlanRef = createResourcePlan.getObjectRef();
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		setResourcePlanDateUnitEffortList(project, resourcePlanRef, dateUnitEffortList);

		CommandSetObjectData appendResourcePlan = CommandSetObjectData.createAppendIdCommand(baseObjectForRow, BaseObject.TAG_RESOURCE_PLAN_IDS, resourcePlanRef);
		project.executeCommand(appendResourcePlan);
	}

	private static void setResourcePlanDateUnitEffortList(Project project, ORef resourcePlanRef, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		CommandSetObjectData addEffortList = new CommandSetObjectData(resourcePlanRef, ResourcePlan.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());
		project.executeCommand(addEffortList);
	}
}