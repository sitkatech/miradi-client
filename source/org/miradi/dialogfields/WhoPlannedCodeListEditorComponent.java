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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourcePlan;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.schemas.ResourcePlanSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.views.diagram.CreateAnnotationDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

import java.util.Vector;

public class WhoPlannedCodeListEditorComponent extends AbstractQuestionBasedComponent
{
	public WhoPlannedCodeListEditorComponent(BaseObject parentObjectToUse, ChoiceQuestion questionToUse)
	{
		super(questionToUse, SINGLE_COLUMN);
		
		parentObject = parentObjectToUse;
		updateToggleButtonSelections(getWhoTotalCodes(parentObject));
	}
	
	@Override
	public void toggleButtonStateChanged(ChoiceItem choiceItem, boolean isSelected)	throws Exception
	{
		CodeList currentCodes = getWhoTotalCodes(getParentObject());
		boolean doesCodeExist = currentCodes.contains(choiceItem.getCode());
		final boolean needToDelete = doesCodeExist && !isSelected;
		final boolean needToCreate = !doesCodeExist && isSelected;
		ORef refCode = ORef.createFromString(choiceItem.getCode());
		
		if (needToDelete)
			deleteMatchingResourcePlans(refCode);
		if (needToCreate)
			createResourcePlan(refCode);
	}

	private void deleteMatchingResourcePlans(ORef selectedResourceRef) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			int oldResourceRefsSize = getResourcePlanRefs().size();
			DateUnitEffortList oldDateUnitEffortList = getDateUnitEffortListFromResourcePlan();
			Vector<ResourcePlan> resourcePlansToDelete = extractResourcePlans(selectedResourceRef);
			
			removeResourcePlans(resourcePlansToDelete);
			updateDividedDateUnitEffortList(oldResourceRefsSize, oldDateUnitEffortList);
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private void clearResourceRef(ResourcePlan resourcePlan) throws Exception
	{
		setResourcePlanResource(resourcePlan, ORef.INVALID);
	}

	private void setResourcePlanResource(ResourcePlan resourcePlan, ORef resourceRef) throws Exception
	{
		CommandSetObjectData setResourcePlanResourceRef = new CommandSetObjectData(resourcePlan, ResourcePlan.TAG_RESOURCE_ID, resourceRef.getObjectId().toString());
		getProject().executeCommand(setResourcePlanResourceRef);
	}

	private void removeResourcePlans(Vector<ResourcePlan> resourcePlansToDelete) throws Exception
	{
		for (int index = 0; index < resourcePlansToDelete.size(); ++index)
		{
			ResourcePlan resourcePlan = resourcePlansToDelete.get(index);
			clearResourceRef(resourcePlan);
			if (getResourcePlanRefs().size() > 1)
			{
				CommandVector deleteResourcePlan = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourcePlan, getResourcePlanTag());
				getProject().executeCommands(deleteResourcePlan);
			}
		}
	}

	private void createResourcePlan(ORef resourceRef) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORefList oldResourcePlanRefs = getResourcePlanRefs();
			int oldResourcePlansCount = oldResourcePlanRefs.size();
			DateUnitEffortList oldDateUnitEffortList = getDateUnitEffortListFromResourcePlan();

			ResourcePlan resourcePlanWithoutResource = findResourcePlanWithoutResource();
			if (resourcePlanWithoutResource == null)
				resourcePlanWithoutResource = createNewResourcePlan();
			
			setResourcePlanResource(resourcePlanWithoutResource, resourceRef);
			updateDividedDateUnitEffortList(oldResourcePlansCount, oldDateUnitEffortList);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private ResourcePlan findResourcePlanWithoutResource() throws Exception
	{
		ORef invalidResourceRef = ORef.createInvalidWithType(ProjectResourceSchema.getObjectType());
		Vector<ResourcePlan> resourcePlansWithoutResource = extractResourcePlans(invalidResourceRef);
		if (resourcePlansWithoutResource.size() == 0)
			return null;
		
		return resourcePlansWithoutResource.get(0);
	}
	
	private Vector<ResourcePlan> extractResourcePlans(ORef selectedResourceRef) throws Exception
	{
		ORefList oldResourcePlanRefs = getResourcePlanRefs();
		Vector<ResourcePlan> resourcePlansToDelete = new Vector<ResourcePlan>();
		for (int index = 0; index < oldResourcePlanRefs.size(); ++index)
		{
			ResourcePlan resourcePlan = ResourcePlan.find(getProject(), oldResourcePlanRefs.get(index));
			ORef resourceRef = resourcePlan.getResourceRef();
			if (resourceRef.equals(selectedResourceRef))
				resourcePlansToDelete.add(resourcePlan);
		}
		
		return resourcePlansToDelete;
	}

	private ResourcePlan createNewResourcePlan() throws Exception
	{
		CommandCreateObject createCommand = new CommandCreateObject(ResourcePlanSchema.getObjectType());
		getProject().executeCommand(createCommand);

		ORef newResourcePlanRef = createCommand.getObjectRef();
		Command appendCommand = CreateAnnotationDoer.createAppendCommand(getParentObject(), newResourcePlanRef, getResourcePlanTag());
		getProject().executeCommand(appendCommand);
		
		return ResourcePlan.find(getProject(), newResourcePlanRef);
	}

	private void updateDividedDateUnitEffortList(int oldResourcePlanCount, DateUnitEffortList oldDateUnitEffortList) throws Exception
	{
		ORefList newResourcePlanRefs = getResourcePlanRefs();
		DateUnitEffortList templateDateUnitEffortList = createTemplateDateUnitEffortList(oldDateUnitEffortList, oldResourcePlanCount, newResourcePlanRefs.size());
		updateDateUnitEffortLists(newResourcePlanRefs, templateDateUnitEffortList);
	}

	private DateUnitEffortList createTemplateDateUnitEffortList(DateUnitEffortList oldDateUnitEffortList, int oldResourcePlanCount, int newResourcePlanCount) throws Exception
	{
		DateUnitEffortList newDateUnitEffortList = new DateUnitEffortList();
		for (int index = 0; index < oldDateUnitEffortList.size(); ++index)
		{
			DateUnitEffort oldDateUnitEffort = oldDateUnitEffortList.getDateUnitEffort(index);
			double oldTotalUnits = oldDateUnitEffort.getQuantity() * oldResourcePlanCount;
			double newUnitQuantity = oldTotalUnits / newResourcePlanCount;
			DateUnitEffort newDateUnitEffort = new DateUnitEffort(oldDateUnitEffort.getDateUnit(), newUnitQuantity);
			newDateUnitEffortList.add(newDateUnitEffort);
		}
		
		return newDateUnitEffortList;
	}
	
	private void updateDateUnitEffortLists(ORefList newResourcePlanRefs, DateUnitEffortList templateDateUnitEffortList) throws Exception
	{
		for (int index = 0; index < newResourcePlanRefs.size(); ++index)
		{
			ResourcePlan resourcePlan = ResourcePlan.find(getProject(), newResourcePlanRefs.get(index));
			CommandSetObjectData setDateUnitEffortList = new CommandSetObjectData(resourcePlan, ResourcePlan.TAG_DATEUNIT_EFFORTS, templateDateUnitEffortList.toString());
			getProject().executeCommand(setDateUnitEffortList);
		}
	}
	
	private DateUnitEffortList getDateUnitEffortListFromResourcePlan() throws Exception
	{
		ORefList existingResourcePlanRefs = getResourcePlanRefs();
		if (existingResourcePlanRefs.isEmpty())
			return new DateUnitEffortList();

		ResourcePlan firstResourcePlan = ResourcePlan.find(getProject(), existingResourcePlanRefs.get(0));
		return firstResourcePlan.getDateUnitEffortList();
	}
	
	public static CodeList getWhoTotalCodes(BaseObject baseObject)
	{		
		try
		{
			ORefSet resourcesRefs = baseObject.getTotalTimePeriodCostsMapForPlans().getAllProjectResourceRefs();
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
	
	private ORefList getResourcePlanRefs() throws Exception
	{
		return getParentObject().getSafeRefListData(getResourcePlanTag());
	}
	
	private String getResourcePlanTag()
	{
		return BaseObject.TAG_RESOURCE_PLAN_IDS;
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
