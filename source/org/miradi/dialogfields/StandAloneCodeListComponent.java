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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.views.diagram.CreateAnnotationDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

public class StandAloneCodeListComponent extends AbstractQuestionBasedComponent
{
	public StandAloneCodeListComponent(BaseObject parentObjectToUse, ChoiceQuestion questionToUse)
	{
		super(questionToUse, LAYOUT_COLUMN_COUNT, null);
		
		parentObject = parentObjectToUse;
		updateToggleButtonSelections(getWhoTotalCodes(parentObject));
	}
	
	@Override
	public void valueChanged(ChoiceItem choiceItem, boolean isSelected)	throws Exception
	{
		CodeList currentCodes = getWhoTotalCodes(getParentObject());
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
				Vector<Command> deleteResourceAssignment = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourceAssignment, getResourceAssignmentTag());
				getProject().executeCommandsWithoutTransaction(deleteResourceAssignment);
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
		ORef invalidResourceRef = ORef.createInvalidWithType(ProjectResource.getObjectType());
		Vector<ResourceAssignment> resourceAssignmentsWithoutResource = extractResourceAssignments(invalidResourceRef);
		if (resourceAssignmentsWithoutResource.size() == 0)
			return null;
		
		return resourceAssignmentsWithoutResource.get(0);
	}
	
	private Vector<ResourceAssignment> extractResourceAssignments(ORef selectedResourceRef) throws Exception
	{
		ORefList oldResourceAssignmentRefs = getResourceAssignmentRefs();
		Vector<ResourceAssignment> resourceAssignmentsToDelete = new Vector();
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
		CommandCreateObject createCommand = new CommandCreateObject(ResourceAssignment.getObjectType());
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
			CommandSetObjectData setDateUnitEffortList = new CommandSetObjectData(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, templateDateUnitEffortList.toString());
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
	
	public CodeList getWhoTotalCodes(BaseObject baseObject)
	{		
		try
		{
			ORefSet resourcesRefs = baseObject.getTotalTimePeriodCostsMap().getAllProjectResourceRefs();		
			CodeList projectResourceCodes = new CodeList();
			for(ORef resourceRef : resourcesRefs)
			{
				projectResourceCodes.add(resourceRef.toString());
			}

			return projectResourceCodes;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new CodeList();
		}
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
