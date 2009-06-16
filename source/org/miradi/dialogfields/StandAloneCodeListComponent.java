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
import org.miradi.views.diagram.CreateAnnotationDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;
//FIXME urgent - this class is still under construction,  needs to add and remove assignments from parentObject
public class StandAloneCodeListComponent extends AbstractCodeListComponent
{
	public StandAloneCodeListComponent(BaseObject parentObjectToUse, ChoiceQuestion questionToUse)
	{
		super(questionToUse, LAYOUT_COLUMN_COUNT, null);
		
		parentObject = parentObjectToUse;
		createCheckBoxes(parentObjectToUse.getWhoTotalCodes());
	}
	
	@Override
	public void valueChanged(ChoiceItem choiceItem, boolean isSelected)	throws Exception
	{
		CodeList currentCodes = getParentObject().getWhoTotalCodes();
		if (currentCodes.contains(choiceItem.getCode()) && !isSelected)
			deleteMatchingResourceAssignments(choiceItem);  
		else if (!currentCodes.contains(choiceItem.getCode()))
			createResourceExpense(ORef.createFromString(choiceItem.getCode()));
	}

	private void createResourceExpense(ORef resourceRef) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject createCommand = new CommandCreateObject(ResourceAssignment.getObjectType());
			getProject().executeCommand(createCommand);

			ORef newResourceAssignmentRef = createCommand.getObjectRef();
			cloneFromExistingObject(resourceRef, newResourceAssignmentRef);

			Command appendCommand = CreateAnnotationDoer.createAppendCommand(getParentObject(), newResourceAssignmentRef, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
			getProject().executeCommand(appendCommand);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void cloneFromExistingObject(ORef resourceRef, ORef newResourceAssignmentRef)	throws Exception
	{
		ORefList existingResourceAssignmentRefs = getParentObject().getRefList(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
		ORef existingResourceAssignmentRef = existingResourceAssignmentRefs.getRefForType(ResourceAssignment.getObjectType());
		if (existingResourceAssignmentRef.isValid())
		{
			ResourceAssignment existingResourceAssignment = ResourceAssignment.find(getProject(), existingResourceAssignmentRef);
			getProject().executeCommandsWithoutTransaction(existingResourceAssignment.createCommandsToClone(newResourceAssignmentRef.getObjectId()));
		}
		
		CommandSetObjectData setResouce = new CommandSetObjectData(newResourceAssignmentRef, ResourceAssignment.TAG_RESOURCE_ID, resourceRef.getObjectId().toString());
		getProject().executeCommand(setResouce);
	}

	private BaseObject getParentObject()
	{
		return parentObject;
	}

	private void deleteMatchingResourceAssignments(ChoiceItem choiceItem) throws Exception
	{
		Vector<Command> commands = new Vector();
		ORefList resourceAssignmentRefs = getParentObject().getRefList(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{
			ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRefs.get(index));
			ORef resourceRef = resourceAssignment.getResourceRef();
			if (resourceRef.equals(ORef.createFromString(choiceItem.getCode())))
				commands.addAll(TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourceAssignment, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS));
		}
		
		getProject().executeCommandsAsTransaction(commands);
	}
	
	private Project getProject()
	{
		return getParentObject().getProject();
	}
	
	private static final int LAYOUT_COLUMN_COUNT = 1;
	private BaseObject parentObject;
}
