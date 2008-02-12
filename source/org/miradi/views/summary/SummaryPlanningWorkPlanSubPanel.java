/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
		return EAM.text("Planning: Work Plan");
	}

}
