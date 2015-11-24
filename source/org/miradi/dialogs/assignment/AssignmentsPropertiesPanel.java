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
package org.miradi.dialogs.assignment;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.planning.propertiesPanel.ResourceAssignmentEditorComponent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.utils.FillerLabel;
import org.miradi.views.umbrella.ObjectPicker;

public class AssignmentsPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public AssignmentsPropertiesPanel(MainWindow mainWindowToUse, int objectType, ObjectPicker picker) throws Exception
	{
		super(mainWindowToUse.getProject(), objectType);

		assignmentEditor = new ResourceAssignmentEditorComponent(mainWindowToUse, picker);
		createSingleSection("");

		ObjectDataInputField leaderDropDownField = createAssignedLeaderDropDownField(objectType, BaseObject.TAG_ASSIGNED_LEADER_RESOURCE);
		addFieldsOnOneLine("", new ObjectDataInputField[]{leaderDropDownField});

		add(new FillerLabel());

		add(assignmentEditor);
		updateFieldsFromProject();
	}

	@Override
	public void dispose()
	{
		assignmentEditor.dispose();
		assignmentEditor = null;

		super.dispose();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		assignmentEditor.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		assignmentEditor.becomeInactive();
		super.becomeInactive();
	}
	
	@Override
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		super.setObjectRefs(hierarchyToSelectedRef);
		assignmentEditor.setObjectRefs(hierarchyToSelectedRef);
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Assignments");
	}

	@Override
	protected boolean doesSectionContainFieldWithTag(String tag)
	{
		if (tag.equals(WorkPlanColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE))
			return true;
		
		if (tag.equals(CustomPlanningColumnsQuestion.META_ASSIGNED_WHO_TOTAL))
			return true;

		if (tag.equals(CustomPlanningColumnsQuestion.META_ASSIGNED_WHEN_TOTAL))
			return true;

		return super.doesSectionContainFieldWithTag(tag);
	}
	
	private ResourceAssignmentEditorComponent assignmentEditor;
}
