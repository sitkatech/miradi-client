/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.HtmlUtilities;

public class TimeframePropertiesSubPanel extends ObjectDataInputPanel
{
	public TimeframePropertiesSubPanel(Project projectToUse, ORef orefToUse) throws Exception
	{
		super(projectToUse, orefToUse);

		currentRef = orefToUse;
		planningPreferencesChangeHandler = new PlanningPreferencesChangeHandler();

		getProject().addCommandExecutedListener(planningPreferencesChangeHandler);

		rebuild(orefToUse);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		if (planningPreferencesChangeHandler != null)
			getProject().removeCommandExecutedListener(planningPreferencesChangeHandler);
	}

	@Override
	public void becomeActive()
	{
		super.becomeActive();
		try
		{
			if (planningPreferencesChangeHandler.getRebuildRequired())
			{
				rebuild(currentRef);
				planningPreferencesChangeHandler.setRebuildRequired(false);
			}
		} catch (Exception e)
		{
			EAM.panic(e);
		}
	}

	@Override
	protected boolean doesSectionContainFieldWithTag(String tag)
	{
		if (tag.equals(BaseObject.PSEUDO_TAG_TIMEFRAME_TOTAL))
			return true;

		return super.doesSectionContainFieldWithTag(tag);
	}

	@Override
	public String getPanelDescription()
	{
		return getDefaultTimeframeLabelText();
	}

	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
			super.commandExecuted(event);
			planningPreferencesChangeHandler.commandExecuted(event);

			becomeInactive();
			becomeActive();
	}

	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);

		ORef selectedRef = getSelectedRef();
		if (selectedRef.isValid())
		{
			BaseObject selectedObject = getProject().findObject(selectedRef);
			String objectFullNameAsPlainText = HtmlUtilities.convertHtmlToPlainText(selectedObject.getFullName());
			if (!objectFullNameAsPlainText.isEmpty())
				timeframeLabel.setText(getDefaultTimeframeLabelText() + " - " + objectFullNameAsPlainText);
		}
	}

	private void rebuild(ORef orefToUse) throws Exception
	{
		removeAll();
		getFields().clear();

		timeframeLabel = new PanelTitleLabel(getDefaultTimeframeLabelText());
		addFieldsOnOneLine(timeframeLabel, new Object[]{});

		addFieldWithoutLabel(createTimeframeEditorField(orefToUse));
		add(new FillerLabel());

		PanelTitleLabel totalLabel = new PanelTitleLabel(EAM.text("Timeframe including nested actions"));
		addFieldsOnOneLine(totalLabel, new Object[]{});

		addFieldWithoutLabel(createReadonlyTextField(BaseObject.PSEUDO_TAG_TIMEFRAME_TOTAL));

		updateFieldsFromProject();

		doLayout();

		validate();
		repaint();
	}

	private String getDefaultTimeframeLabelText()
	{
		return EAM.text("Timeframe");
	}

	private ORef getSelectedRef()
	{
		if (getPicker().getSelectedHierarchies().length == 0)
			return ORef.INVALID;

		ORefList selectedObjectRefs = getPicker().getSelectedHierarchies()[0];
		if (selectedObjectRefs.size() == 0)
			return ORef.INVALID;

		ORef selectedObjectRef = selectedObjectRefs.get(0);
		if (selectedObjectRef.isInvalid())
			return ORef.INVALID;

		return selectedObjectRef;
	}

	private class PlanningPreferencesChangeHandler implements CommandExecutedListener
	{

		public PlanningPreferencesChangeHandler()
		{
			rebuildRequired = false;
		}

		@Override
		public void commandExecuted(CommandExecutedEvent event)
		{
			if (eventForcesRebuild(event))
				rebuildRequired = true;
		}

		public boolean getRebuildRequired()
		{
			return rebuildRequired;
		}

		public void setRebuildRequired(boolean rebuildRequiredToUse)
		{
			rebuildRequired = rebuildRequiredToUse;
		}

		private boolean eventForcesRebuild(CommandExecutedEvent event)
		{
			if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY))
				return true;

			if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_DAY_COLUMNS_VISIBILITY))
				return true;

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

		private boolean rebuildRequired;
	}

	private ORef currentRef;
	private PanelTitleLabel timeframeLabel;
	private PlanningPreferencesChangeHandler planningPreferencesChangeHandler;
}
