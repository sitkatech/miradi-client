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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.questions.AssignedDateUnitTypeQuestion;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

public class WhenAssignedEditorComponent extends WhenEditorComponent
{
	public WhenAssignedEditorComponent(Project project, BaseObject baseObjectToUse) throws Exception
	{
		super(project, baseObjectToUse.getResourceAssignmentRefs(), new AssignedDateUnitTypeQuestion(project, baseObjectToUse.getResourceAssignmentRefs()));
	}

	@Override
	public DateUnitEffortList getDateUnitEffortList(ORef planningObjectRef) throws Exception
	{
		ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), planningObjectRef);
		return resourceAssignment.getDateUnitEffortList();
	}

	@Override
	protected TimePeriodCostsMap getTimePeriodCostsMap(ORef planningObjectRef) throws Exception
	{
		ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), planningObjectRef);
		return resourceAssignment.getResourceAssignmentsTimePeriodCostsMap();
	}

	@Override
	protected String getPanelTitle()
	{
		return EAM.text("<html>" +
				"Specifying the Work Unit time span using this dialog <br>" +
				"will enter zeros in the designated time period column(s).");
	}

	public static void setWhenAssignedValue(Project project, BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		project.executeBeginTransaction();
		try
		{
			clearResourceAssignmentDateUnitEfforts(project, baseObjectForRow);
			ORefList resourceAssignmentRefs = baseObjectForRow.getResourceAssignmentRefs();
			if (datesAsCodeList.hasData() && resourceAssignmentRefs.isEmpty())
				createResourceAssignment(project, baseObjectForRow, datesAsCodeList);

			if (datesAsCodeList.hasData() && resourceAssignmentRefs.hasRefs())
				updateResourceAssignments(project, resourceAssignmentRefs, datesAsCodeList);

			if (datesAsCodeList.isEmpty() && resourceAssignmentRefs.size() == 1)
				deleteEmptyResourceAssignment(project, resourceAssignmentRefs.getFirstElement());
		}
		finally
		{
			project.executeEndTransaction();
		}
	}

	private static void updateResourceAssignments(Project project, ORefList resourceAssignmentRefs, CodeList datesAsCodeList) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{
			setResourceAssignmentDateUnitEffortList(project, resourceAssignmentRefs.get(index), dateUnitEffortList);
		}
	}

	private static void clearResourceAssignmentDateUnitEfforts(Project project, BaseObject baseObjectForRow) throws Exception
	{
		ORefList resourceAssignmentRefs = baseObjectForRow.getResourceAssignmentRefs();
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{
			ResourceAssignment resourceAssignment = ResourceAssignment.find(project, resourceAssignmentRefs.get(index));
			Command clearDateUnitEffortList = new CommandSetObjectData(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, new DateUnitEffortList().toString());
			project.executeCommand(clearDateUnitEffortList);
		}
	}

	private static void deleteEmptyResourceAssignment(Project project, ORef resourceAssignmentRef) throws Exception
	{
		ResourceAssignment resourceAssignment = ResourceAssignment.find(project, resourceAssignmentRef);
		if (resourceAssignment.isEmpty())
		{
			CommandVector removeAssignmentCommands = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(project, resourceAssignment, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
			project.executeCommands(removeAssignmentCommands);
		}
	}

	private static void createResourceAssignment(Project project, BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		CommandCreateObject createResourceAssignment = new CommandCreateObject(ResourceAssignmentSchema.getObjectType());
		project.executeCommand(createResourceAssignment);

		ORef resourceAssignmentRef = createResourceAssignment.getObjectRef();
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		setResourceAssignmentDateUnitEffortList(project, resourceAssignmentRef, dateUnitEffortList);

		CommandSetObjectData appendResourceAssignment = CommandSetObjectData.createAppendIdCommand(baseObjectForRow, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, resourceAssignmentRef);
		project.executeCommand(appendResourceAssignment);
	}

	private static void setResourceAssignmentDateUnitEffortList(Project project, ORef resourceAssignmentRef, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		CommandSetObjectData addEffortList = new CommandSetObjectData(resourceAssignmentRef, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
		project.executeCommand(addEffortList);
	}

}