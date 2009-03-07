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
package org.miradi.views.summary;

import org.miradi.dialogfields.ObjectBudgetTimePeriodChoiceField;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.BudgetTimePeriodQuestion;
import org.miradi.questions.FiscalYearStartQuestion;

public class SummaryPlanningWorkPlanSubPanel extends ObjectDataInputPanel
{
	public SummaryPlanningWorkPlanSubPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		ObjectDataInputField startDate = createDateChooserField(ProjectMetadata.TAG_START_DATE);
		ObjectDataInputField endDate = createDateChooserField(ProjectMetadata.TAG_EXPECTED_END_DATE);
		ObjectDataInputField[] projectDateFields = new ObjectDataInputField[] {startDate, endDate, };
		addFieldsOnOneLine(EAM.text("Label|Project Dates"), projectDateFields);
		
		ObjectDataInputField workPlanStartDate = createDateChooserField(ProjectMetadata.TAG_WORKPLAN_START_DATE);
		ObjectDataInputField workPlanEndDate = createDateChooserField(ProjectMetadata.TAG_WORKPLAN_END_DATE);
		ObjectDataInputField[] workPlanDateFields = new ObjectDataInputField[] {workPlanStartDate, workPlanEndDate, };
		addFieldsOnOneLine(EAM.text("Label|Workplan Dates"), workPlanDateFields);
		
		ObjectBudgetTimePeriodChoiceField objectBudgetTimePeriodChoiceField = new ObjectBudgetTimePeriodChoiceField(getProject(), ProjectMetadata.getObjectType(), BaseId.INVALID, ProjectMetadata.TAG_WORKPLAN_TIME_UNIT, new BudgetTimePeriodQuestion());
		addField(objectBudgetTimePeriodChoiceField);
		addField(createChoiceField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_FISCAL_YEAR_START, new FiscalYearStartQuestion()));
		addField(createMultilineField(ProjectMetadata.TAG_PLANNING_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Work Plan Settings");
	}

}
