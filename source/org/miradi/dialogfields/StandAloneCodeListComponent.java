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
		if (doesAssignmentExist && !isSelected)
			deleteMatchingResourceAssignments(choiceItem);  
		else if (!doesAssignmentExist)
			createResourceAssignment(ORef.createFromString(choiceItem.getCode()));
	}

	private void deleteMatchingResourceAssignments(ChoiceItem choiceItem) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			DateUnitEffortList dateUnitEffortList = getAnExistingDateUnitEffortList();
			Vector<Command> commands = new Vector();
			ORefList resourceAssignmentRefs = getParentObject().getRefList(getResourceAssignmentTag());
			
			for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
			{
				ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRefs.get(index));
				ORef resourceRef = resourceAssignment.getResourceRef();
				ORef selectedResourceRef = ORef.createFromString(choiceItem.getCode());
				if (resourceRef.equals(selectedResourceRef))
					commands.addAll(TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourceAssignment, getResourceAssignmentTag()));
			}

			getProject().executeCommandsWithoutTransaction(commands);
			
			final int DELTA_PORTION_AMOUNT = 1;
			updateDateUnitEffortLists(dateUnitEffortList, DELTA_PORTION_AMOUNT);
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
			DateUnitEffortList dateUnitEffortList = getAnExistingDateUnitEffortList(); 
			CommandCreateObject createCommand = new CommandCreateObject(ResourceAssignment.getObjectType());
			getProject().executeCommand(createCommand);

			ORef newResourceAssignmentRef = createCommand.getObjectRef();
			CommandSetObjectData setResouce = new CommandSetObjectData(newResourceAssignmentRef, ResourceAssignment.TAG_RESOURCE_ID, resourceRef.getObjectId().toString());
			getProject().executeCommand(setResouce);

			Command appendCommand = CreateAnnotationDoer.createAppendCommand(getParentObject(), newResourceAssignmentRef, getResourceAssignmentTag());
			getProject().executeCommand(appendCommand);
			
			final int DELTA_PORTION_AMOUNT = -1;
			updateDateUnitEffortLists(dateUnitEffortList, DELTA_PORTION_AMOUNT);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void updateDateUnitEffortLists(DateUnitEffortList dateUnitEffortList, final int deltaPortionAmount) throws Exception
	{
		ORefList updatedResourceAssignmentRefs = getParentObject().getRefList(getResourceAssignmentTag());
		for (int index = 0; index < updatedResourceAssignmentRefs.size(); ++index)
		{
			ResourceAssignment thisResourceAssignment = ResourceAssignment.find(getProject(), updatedResourceAssignmentRefs.get(index));
			updateDateUnitEffortList(dateUnitEffortList, thisResourceAssignment, updatedResourceAssignmentRefs.size(), deltaPortionAmount);
		}
	}
	
	private void updateDateUnitEffortList(DateUnitEffortList dateUnitEffortList, ResourceAssignment resourceAssignment, int portion, int portionDelta) throws Exception
	{		
		DateUnitEffortList newDateUnitEffortList = new DateUnitEffortList();
		for (int index = 0; index < dateUnitEffortList.size(); ++index)
		{
			DateUnitEffort dateUnitEffort = dateUnitEffortList.getDateUnitEffort(index);
			double newUnitQuantity = ((dateUnitEffort.getQuantity() * (portion + portionDelta)) / portion);
			DateUnitEffort newDateUnitEffort = new DateUnitEffort(newUnitQuantity, dateUnitEffort.getDateUnit());
			newDateUnitEffortList.add(newDateUnitEffort);
		}
		
		CommandSetObjectData setDateUnitEffortList = new CommandSetObjectData(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, newDateUnitEffortList.toString());
		getProject().executeCommand(setDateUnitEffortList);
	}
	
	private DateUnitEffortList getAnExistingDateUnitEffortList() throws Exception
	{
		ORefList existingResourceAssignmentRefs = getParentObject().getRefList(getResourceAssignmentTag());
		ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), existingResourceAssignmentRefs.getRefForType(ResourceAssignment.getObjectType()));
		
		return resourceAssignment.getDateUnitEffortList();
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
