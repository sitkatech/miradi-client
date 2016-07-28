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
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.*;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.OptionalDouble;
import org.miradi.views.diagram.CreateAnnotationDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

import java.util.Vector;

public class WhoAssignedCodeListEditorComponent extends AbstractQuestionBasedComponent
{
	public WhoAssignedCodeListEditorComponent(BaseObject parentObjectToUse, ChoiceQuestion questionToUse, ORefSet resourceRefsFilterToUse)
	{
		super(questionToUse, SINGLE_COLUMN);
		
		parentObject = parentObjectToUse;
		resourceRefsFilter = resourceRefsFilterToUse;
		updateToggleButtonSelections(parentObject.getAssignedWhoResourcesAsCodeList());
		setInstructions();
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

		setInstructions();
	}

	@Override
	protected void addAdditionalComponent()
	{
		instructionsPanel = new PanelTitleLabel(getInstructions());
		add(instructionsPanel);
	}

	private void setInstructions()
	{
		if (instructionsPanel != null)
			instructionsPanel.setText(getInstructions());
	}

	private String getInstructions()
	{
		StringBuilder instructionsText = new StringBuilder();

		instructionsText.append("<html>");
		instructionsText.append("<div style='margin-left: 7px; margin-right: 7px;'>");
		instructionsText.append(EAM.text("People assigned to this action and actions below it, based on current filter settings: "));
		instructionsText.append("</div>");
		if (parentObject != null)
		{
			instructionsText.append("<div style='margin-left: 7px;'>");
			instructionsText.append(parentObject.getAssignedWhoRollupResourcesAsString(resourceRefsFilter));
			instructionsText.append("</div>");
		}
		instructionsText.append("<div style='margin-left: 7px;'>");
		instructionsText.append(EAM.text("People assigned to this action only: "));
		instructionsText.append("</div>");
		instructionsText.append("</html>");

		return instructionsText.toString();
	}

	private void deleteMatchingResourceAssignments(ORef resourceRef) throws Exception
	{
		boolean hasWorkUnitData = false;
		Vector<ResourceAssignment> resourceAssignmentsToDelete = extractResourceAssignments(resourceRef);

		for (ResourceAssignment resourceAssignment : resourceAssignmentsToDelete)
		{
			TimePeriodCostsMap timePeriodCostsMap = resourceAssignment.getTotalTimePeriodCostsMapForAssignments();
			TimePeriodCosts wholeProjectTimePeriodCosts = timePeriodCostsMap.calculateTimePeriodCosts(new DateUnit());
			OptionalDouble totalWorkUnits = wholeProjectTimePeriodCosts.getTotalWorkUnits();
			if (totalWorkUnits.hasNonZeroValue())
			{
				hasWorkUnitData = true;
				break;
			}
		}

		Vector<String> dialogText = new Vector<String>();

		if (hasWorkUnitData)
		{
			dialogText.add(EAM.text("Work units have been entered for this assignment. All work units will be removed. Continue?"));
			String[] buttons = {EAM.text("Yes"), EAM.text("No"), };
			if(!EAM.confirmDialog(EAM.text("Delete Work Assignments"), dialogText.toArray(new String[0]), buttons))
			{
				updateToggleButtonSelections(parentObject.getAssignedWhoResourcesAsCodeList());
				return;
			}
		}

		deleteMatchingResourceAssignments(resourceAssignmentsToDelete);
	}

	private void deleteMatchingResourceAssignments(Vector<ResourceAssignment> resourceAssignmentsToDelete) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			removeResourceAssignments(resourceAssignmentsToDelete);
		}
		finally
		{
			getProject().executeEndTransaction();
		}
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
			CommandVector deleteResourceAssignment = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), resourceAssignment, getResourceAssignmentTag());
			getProject().executeCommands(deleteResourceAssignment);
		}
	}

	private void createResourceAssignment(ORef resourceRef) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ResourceAssignment resourceAssignmentWithoutResource = findResourceAssignmentWithoutResource();
			if (resourceAssignmentWithoutResource == null)
				resourceAssignmentWithoutResource = createNewResourceAssignment();
			
			setResourceAssignmentResource(resourceAssignmentWithoutResource, resourceRef);
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
	private PanelTitleLabel instructionsPanel;
	private ORefSet resourceRefsFilter;
}
