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

package org.miradi.dialogfields;

import org.miradi.commands.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.views.diagram.CreateAnnotationDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

import java.util.Vector;

public class WhoAssignedCodeListEditorComponent extends AbstractQuestionBasedComponent
{
	public WhoAssignedCodeListEditorComponent(BaseObject parentObjectToUse, ChoiceQuestion questionToUse)
	{
		super(questionToUse, SINGLE_COLUMN);
		
		parentObject = parentObjectToUse;
		updateToggleButtonSelections(parentObject.getAssignedWhoResourcesAsCodeList());
	}
	
	@Override
	public void toggleButtonStateChanged(ChoiceItem choiceItem, boolean isSelected)	throws Exception
	{
		CodeList currentCodes = getParentObject().getAssignedWhoResourcesAsCodeList();
		boolean doesAssignmentExist = currentCodes.contains(choiceItem.getCode());
		final boolean needToDelete = doesAssignmentExist && !isSelected;
		final boolean needToCreate = !doesAssignmentExist && isSelected;
		ORef refCode = ORef.createFromString(choiceItem.getCode());
		
		if (needToDelete)
			deleteMatchingResourceAssignments(refCode);
		if (needToCreate)
			createResourceAssignment(refCode);
	}

	private void deleteMatchingResourceAssignments(ORef selectedResourceRef ) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			int oldResourceRefsSize = getResourceAssignmentRefs().size();
			DateUnitEffortList oldDateUnitEffortList = getDateUnitEffortListFromResourceAssignment();
			Vector<ResourceAssignment> resourceAssignmentsToDelete = extractResourceAssignments(selectedResourceRef);
			
			removeResourceAssignments(resourceAssignmentsToDelete);
			updateDividedDateUnitEffortList(oldResourceRefsSize, oldDateUnitEffortList);
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private void clearResourceRef(ResourceAssignment resourceAssignment) throws Exception
	{
		setResourceAssignmentResource(resourceAssignment, ORef.INVALID);
	}

	private void setResourceAssignmentResource(ResourceAssignment resourceAssignment, ORef resourceRef) throws Exception
	{
		CommandSetObjectData clearProjectResourceRef = new CommandSetObjectData(resourceAssignment, ResourceAssignment.TAG_RESOURCE_ID, resourceRef.getObjectId().toString());
		getProject().executeCommand(clearProjectResourceRef);
	}

	private void removeResourceAssignments(Vector<ResourceAssignment> resourceAssignmentsToDelete) throws Exception
	{
		for (int index = 0; index < resourceAssignmentsToDelete.size(); ++index)
		{
			ResourceAssignment resourceAssignment = resourceAssignmentsToDelete.get(index);
			clearResourceRef(resourceAssignment);
			if (getResourceAssignmentRefs().size() > 1)
			{
				CommandVector deleteResourceAssignment = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourceAssignment, getResourceAssignmentTag());
				getProject().executeCommands(deleteResourceAssignment);
			}
		}
	}

	private void createResourceAssignment(ORef resourceRef) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORefList oldResourceAssignmentRefs = getResourceAssignmentRefs();
			int oldResourceAssignmentsCount = oldResourceAssignmentRefs.size();
			DateUnitEffortList oldDateUnitEffortList = getDateUnitEffortListFromResourceAssignment(); 	

			ResourceAssignment resourceAssignmentWithoutResource = findResourceAssignmentWithoutResource();
			if (resourceAssignmentWithoutResource == null)
				resourceAssignmentWithoutResource = createNewResourceAssignment();
			
			setResourceAssignmentResource(resourceAssignmentWithoutResource, resourceRef);
			updateDividedDateUnitEffortList(oldResourceAssignmentsCount, oldDateUnitEffortList);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private ResourceAssignment findResourceAssignmentWithoutResource() throws Exception
	{
		ORef invalidResourceRef = ORef.createInvalidWithType(ProjectResourceSchema.getObjectType());
		Vector<ResourceAssignment> resourceAssignmentsWithoutResource = extractResourceAssignments(invalidResourceRef);
		if (resourceAssignmentsWithoutResource.size() == 0)
			return null;
		
		return resourceAssignmentsWithoutResource.get(0);
	}
	
	private Vector<ResourceAssignment> extractResourceAssignments(ORef selectedResourceRef) throws Exception
	{
		ORefList oldResourceAssignmentRefs = getResourceAssignmentRefs();
		Vector<ResourceAssignment> resourceAssignmentsToDelete = new Vector<ResourceAssignment>();
		for (int index = 0; index < oldResourceAssignmentRefs.size(); ++index)
		{
			ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), oldResourceAssignmentRefs.get(index));
			ORef resourceRef = resourceAssignment.getResourceRef();
			if (resourceRef.equals(selectedResourceRef))
				resourceAssignmentsToDelete.add(resourceAssignment);
		}
		
		return resourceAssignmentsToDelete;
	}

	private ResourceAssignment createNewResourceAssignment() throws Exception
	{
		CommandCreateObject createCommand = new CommandCreateObject(ResourceAssignmentSchema.getObjectType());
		getProject().executeCommand(createCommand);

		ORef newResourceAssignmentRef = createCommand.getObjectRef();
		Command appendCommand = CreateAnnotationDoer.createAppendCommand(getParentObject(), newResourceAssignmentRef, getResourceAssignmentTag());
		getProject().executeCommand(appendCommand);
		
		return ResourceAssignment.find(getProject(), newResourceAssignmentRef);
	}

	private void updateDividedDateUnitEffortList(int oldResourceAssignmentCount, DateUnitEffortList oldDateUnitEffortList) throws Exception
	{
		ORefList newResourceAssignmentRefs = getResourceAssignmentRefs();
		DateUnitEffortList templateDateUnitEffortList = createTemplateDateUnitEffortList(oldDateUnitEffortList, oldResourceAssignmentCount, newResourceAssignmentRefs.size());		
		updateDateUnitEffortLists(newResourceAssignmentRefs, templateDateUnitEffortList);
	}

	private DateUnitEffortList createTemplateDateUnitEffortList(DateUnitEffortList oldDateUnitEffortList, int oldResourceAssignmentCount, int newResourceAssignmentCount) throws Exception
	{
		DateUnitEffortList newDateUnitEffortList = new DateUnitEffortList();
		for (int index = 0; index < oldDateUnitEffortList.size(); ++index)
		{
			DateUnitEffort oldDateUnitEffort = oldDateUnitEffortList.getDateUnitEffort(index);
			double oldTotalUnits = oldDateUnitEffort.getQuantity() * oldResourceAssignmentCount;
			double newUnitQuantity = oldTotalUnits / newResourceAssignmentCount; 
			DateUnitEffort newDateUnitEffort = new DateUnitEffort(oldDateUnitEffort.getDateUnit(), newUnitQuantity);
			newDateUnitEffortList.add(newDateUnitEffort);
		}
		
		return newDateUnitEffortList;
	}
	
	private void updateDateUnitEffortLists(ORefList newResourceAssignmentRefs, DateUnitEffortList templateDateUnitEffortList) throws Exception
	{
		for (int index = 0; index < newResourceAssignmentRefs.size(); ++index)
		{
			ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), newResourceAssignmentRefs.get(index));
			CommandSetObjectData setDateUnitEffortList = new CommandSetObjectData(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, templateDateUnitEffortList.toString());
			getProject().executeCommand(setDateUnitEffortList);
		}
	}
	
	private DateUnitEffortList getDateUnitEffortListFromResourceAssignment() throws Exception
	{
		ORefList existingResourceAssignmentRefs = getResourceAssignmentRefs();
		if (existingResourceAssignmentRefs.isEmpty())
			return new DateUnitEffortList();
		
		ResourceAssignment firstResourceAssignment = ResourceAssignment.find(getProject(), existingResourceAssignmentRefs.get(0));
		return firstResourceAssignment.getDateUnitEffortList();
	}
	
	private ORefList getResourceAssignmentRefs() throws Exception
	{
		return getParentObject().getSafeRefListData(getResourceAssignmentTag());
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
	
	private BaseObject parentObject;
}
