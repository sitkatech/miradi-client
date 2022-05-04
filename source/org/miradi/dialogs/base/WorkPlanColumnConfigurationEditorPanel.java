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

package org.miradi.dialogs.base;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.TableSettingsSchema;

public class WorkPlanColumnConfigurationEditorPanel extends ObjectDataInputPanel
{
	public WorkPlanColumnConfigurationEditorPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		createPanel(orefToUse, StaticQuestionManager.getQuestion(WorkPlanVisibleRowsQuestion.class), true);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Work Plan Column Editor");
	}

	private String getConfigurationLabel()
	{
		return EAM.text("Select which rows and column groups to display.");
	}

	private void createPanel(ORef orefToUse, ChoiceQuestion workPlanVisibleRowsQuestion, boolean addRowConfiguration)
	{
		addField(createChoiceField(TableSettingsSchema.getObjectType(), TableSettings.TAG_WORK_PLAN_VISIBLE_NODES_CODE, workPlanVisibleRowsQuestion));

		addHtmlWrappedLabel("");
		addHtmlWrappedLabel(getConfigurationLabel());

		if (addRowConfiguration)
		{
			ChoiceQuestion rowConfigurationQuestion = StaticQuestionManager.getQuestion(WorkPlanRowConfigurationQuestion.class);
			addFieldWithCustomLabel(createWorkPlanCodeListEditor(orefToUse.getObjectType(), TableSettings.TAG_TABLE_SETTINGS_MAP, rowConfigurationQuestion, TableSettings.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY), EAM.text("Rows"));
		}

		ChoiceQuestion columnConfigurationQuestion = StaticQuestionManager.getQuestion(WorkPlanColumnConfigurationQuestion.class);
		addFieldWithCustomLabel(createWorkPlanCodeListEditor(orefToUse.getObjectType(), TableSettings.TAG_TABLE_SETTINGS_MAP, columnConfigurationQuestion, TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY), EAM.text("Column groups"));

		updateFieldsFromProject();
	}
}
