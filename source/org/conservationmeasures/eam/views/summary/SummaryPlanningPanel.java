/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.FiscalYearStartQuestion;

public class SummaryPlanningPanel extends ObjectDataInputPanel
{
	public SummaryPlanningPanel(Project projectToUse, ORef orefToUse)
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
		
		addField(createStringField(ProjectMetadata.TAG_WORKPLAN_TIME_UNIT));
		addField(createChoiceField(ProjectMetadata.getObjectType(), new FiscalYearStartQuestion(ProjectMetadata.TAG_FISCAL_YEAR_START)));
		addField(createMultilineField(ProjectMetadata.TAG_PLANNING_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Planning");
	}
}
