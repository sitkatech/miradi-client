/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.views.workplan;

import org.miradi.dialogfields.AbstractWorkPlanStringMapEditorDoer;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.TableSettings;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.WorkPlanAnalysisConfigurationQuestion;
import org.miradi.questions.WorkPlanCategoryTypesQuestion;
import org.miradi.schemas.TableSettingsSchema;
import org.miradi.utils.FillerLabel;

public class EditAnalysisRowsPanel extends ObjectDataInputPanel
{
	public EditAnalysisRowsPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, projectToUse.getCurrentViewData().getRef());

		setLayout(new OneColumnGridLayout());
		add(new PanelTitleLabel(EAM.text("Specify values in the drop downs to group work and expense assignments for analysis.")));
		add(new FillerLabel());
		addFieldWithoutLabel(createConfigureAnalysisRowsField(getProject().getCurrentViewData().getRef(), ViewData.TAG_BUDGET_ROLLUP_REPORT_TYPES, StaticQuestionManager.getQuestion(WorkPlanCategoryTypesQuestion.class)));
		
		TableSettings workPlanTableSettings = TableSettings.findOrCreate(getProject(), AbstractWorkPlanStringMapEditorDoer.getTabSpecificModelIdentifier());
		ChoiceQuestion columnConfigurationQuestion = StaticQuestionManager.getQuestion(WorkPlanAnalysisConfigurationQuestion.class);
		addFieldWithCustomLabel(createWorkPlanCodeListEditor(TableSettingsSchema.getObjectType(), TableSettings.TAG_TABLE_SETTINGS_MAP, columnConfigurationQuestion, TableSettings.ANALYSIS_BUDGET_COLUMNS_CODELIST_KEY), EAM.text("Column groups"));

		setObjectRefs(new ORef[]{getProject().getCurrentViewData().getRef(), workPlanTableSettings.getRef(), });
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return "BudgetRollupChoiceEditorPanel";
	}
}
