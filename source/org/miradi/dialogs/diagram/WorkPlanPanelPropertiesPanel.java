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

package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.schemas.ProjectMetadataSchema;

import javax.swing.*;

public class WorkPlanPanelPropertiesPanel extends ObjectDataInputPanel
{
	public WorkPlanPanelPropertiesPanel(Project projectToUse, ORef orefToUse) throws Exception
	{
		super(projectToUse, orefToUse);

		currentRef = orefToUse;
		rebuild(orefToUse);
	}

	@Override
	protected boolean doesSectionContainFieldWithTag(String tag)
	{
		if (tag.equals(CustomPlanningColumnsQuestion.META_PLANNED_WHO_TOTAL))
			return true;

		if (tag.equals(BaseObject.PSEUDO_TAG_PLANNED_WHEN_TOTAL))
			return true;

		return super.doesSectionContainFieldWithTag(tag);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Who, When Plan");
	}

	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			super.commandExecuted(event);
			if (eventForcesRebuild(event))
			{
				becomeInactive();
				rebuild(currentRef);
				becomeActive();
			}
		}
		catch (Exception e)
		{
			EAM.panic(e);
		}
	}

	private void rebuild(ORef orefToUse) throws Exception
	{
		removeAll();
		getFields().clear();

		add(new PanelTitleLabel(EAM.text("Who")));
		addFieldWithoutLabel(createWhoPlannedEditorField(orefToUse));

		addField(createReadonlyTextField(BaseObject.PSEUDO_TAG_PLANNED_WHO_TOTAL));

		addField(createPlannedLeaderDropDownField(orefToUse.getObjectType(), BaseObject.TAG_PLANNED_LEADER_RESOURCE));

		PanelTitleLabel label = new PanelTitleLabel(EAM.text("When"));
		label.setVerticalAlignment(SwingConstants.TOP);
		add(label);
		addFieldWithoutLabel(createWhenPlannedEditorField(orefToUse));

		addField(createReadonlyTextField(BaseObject.PSEUDO_TAG_PLANNED_WHEN_TOTAL));

		updateFieldsFromProject();

		doLayout();

		validate();
		repaint();
	}

	private boolean eventForcesRebuild(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_WORKPLAN_END_DATE))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_WORKPLAN_START_DATE))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_START_DATE))
			return true;

		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_EXPECTED_END_DATE))
			return true;

		return false;
	}

	private ORef currentRef;
}
