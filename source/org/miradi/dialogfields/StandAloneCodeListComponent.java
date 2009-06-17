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

package org.miradi.dialogfields;

import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.views.diagram.CreateAnnotationDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

public class StandAloneCodeListComponent extends AbstractCodeListComponent
{
	public StandAloneCodeListComponent(BaseObject parentObjectToUse, ChoiceQuestion questionToUse)
	{
		super(questionToUse, LAYOUT_COLUMN_COUNT, null);
		
		parentObject = parentObjectToUse;
		createCheckBoxes(parentObject.getWhoTotalCodes());
	}
	
	@Override
	public void valueChanged(ChoiceItem choiceItem, boolean isSelected)	throws Exception
	{
		CodeList currentCodes = getParentObject().getWhoTotalCodes();
		boolean doesAssignmentExist = currentCodes.contains(choiceItem.getCode());
		final boolean needToDelete = doesAssignmentExist && !isSelected;
		final boolean needToCreate = !doesAssignmentExist;
		if (needToDelete)
			deleteMatchingResourceAssignments(choiceItem);
		else if (needToCreate)
			createResourceAssignment(ORef.createFromString(choiceItem.getCode()));
	}

	private void deleteMatchingResourceAssignments(ChoiceItem choiceItem) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			DateUnitEffortList oldDateUnitEffortList = getAnExistingDateUnitEffortList();
			ORefList oldResourceAssignmentRefs = getResourceAssignmentRefs();
			
			ORef selectedResourceRef = ORef.createFromString(choiceItem.getCode());
			Vector<Command> commands = new Vector();	
			for (int index = 0; index < oldResourceAssignmentRefs.size(); ++index)
			{
				ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), oldResourceAssignmentRefs.get(index));
				ORef resourceRef = resourceAssignment.getResourceRef();
				if (resourceRef.equals(selectedResourceRef))
					commands.addAll(TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourceAssignment, getResourceAssignmentTag()));
			}

			getProject().executeCommandsWithoutTransaction(commands);
			
			updateDividedDateUnitEffortList(oldResourceAssignmentRefs.size(), oldDateUnitEffortList);
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}
	
	private void createResourceAssignment(ORef resourceRef) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			int oldResourceAssignmentsCount = getResourceAssignmentRefs().size();
			DateUnitEffortList oldDateUnitEffortList = getAnExistingDateUnitEffortList(); 	
	
			CommandCreateObject createCommand = new CommandCreateObject(ResourceAssignment.getObjectType());
			getProject().executeCommand(createCommand);

			ORef newResourceAssignmentRef = createCommand.getObjectRef();
			CommandSetObjectData setResouce = new CommandSetObjectData(newResourceAssignmentRef, ResourceAssignment.TAG_RESOURCE_ID, resourceRef.getObjectId().toString());
			getProject().executeCommand(setResouce);
			
			Command appendCommand = CreateAnnotationDoer.createAppendCommand(getParentObject(), newResourceAssignmentRef, getResourceAssignmentTag());
			getProject().executeCommand(appendCommand);
			
			updateDividedDateUnitEffortList(oldResourceAssignmentsCount, oldDateUnitEffortList);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void updateDividedDateUnitEffortList(int oldResourceAssignmentCount, DateUnitEffortList oldDateUnitEffortList) throws Exception
	{
		ORefList newResourceAssignmentRefs = getResourceAssignmentRefs();
		DateUnitEffortList templateDateUnitEffortList = createTemplateDateUnitEffortList(oldResourceAssignmentCount, newResourceAssignmentRefs.size(), oldDateUnitEffortList);		
		updateDateUnitEffortLists(newResourceAssignmentRefs, templateDateUnitEffortList);
	}

	private DateUnitEffortList createTemplateDateUnitEffortList(int oldResourceAssignmentCount,	int newResourceAssignmentCount, DateUnitEffortList oldDateUnitEffortList) throws Exception
	{
		DateUnitEffortList newDateUnitEffortList = new DateUnitEffortList();
		for (int index = 0; index < oldDateUnitEffortList.size(); ++index)
		{
			DateUnitEffort oldDateUnitEffort = oldDateUnitEffortList.getDateUnitEffort(index);
			double oldTotalUnits = oldDateUnitEffort.getQuantity() * oldResourceAssignmentCount;
			double newUnitQuantity = oldTotalUnits / newResourceAssignmentCount; 
			DateUnitEffort newDateUnitEffort = new DateUnitEffort(newUnitQuantity, oldDateUnitEffort.getDateUnit());
			newDateUnitEffortList.add(newDateUnitEffort);
		}
		
		return newDateUnitEffortList;
	}
	
	private void updateDateUnitEffortLists(ORefList newResourceAssignmentRefs, DateUnitEffortList templateDateUnitEffortList) throws Exception
	{
		for (int index = 0; index < newResourceAssignmentRefs.size(); ++index)
		{
			ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), newResourceAssignmentRefs.get(index));
			CommandSetObjectData setDateUnitEffortList = new CommandSetObjectData(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, templateDateUnitEffortList.toString());
			getProject().executeCommand(setDateUnitEffortList);
		}
	}
	
	private DateUnitEffortList getAnExistingDateUnitEffortList() throws Exception
	{
		ORefList existingResourceAssignmentRefs = getResourceAssignmentRefs();
		if (existingResourceAssignmentRefs.isEmpty())
			return new DateUnitEffortList();
		
		ResourceAssignment firstResourceAssignment = ResourceAssignment.find(getProject(), existingResourceAssignmentRefs.get(0));
		return firstResourceAssignment.getDateUnitEffortList();
	}
	
	private ORefList getResourceAssignmentRefs() throws Exception
	{
		return getParentObject().getRefList(getResourceAssignmentTag());
	}
	
	private String getResourceAssignmentTag()
	{
		return BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS;
	}
	
	private BaseObject getParentObject()
	{
		return parentObject;
	}
	
	private Project getProject()
	{
		return getParentObject().getProject();
	}
	
	private static final int LAYOUT_COLUMN_COUNT = 1;
	private BaseObject parentObject;
}
