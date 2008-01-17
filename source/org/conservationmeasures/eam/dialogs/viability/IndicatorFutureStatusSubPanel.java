/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class IndicatorFutureStatusSubPanel extends ObjectDataInputPanel
{
	public IndicatorFutureStatusSubPanel(Project project)
	{
		this(project, new ORef(Indicator.getObjectType(), BaseId.INVALID));
	}
	
	public IndicatorFutureStatusSubPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);

		ObjectDataInputField futureStatusDateField = createDateChooserField(ObjectType.INDICATOR,  Indicator.TAG_FUTURE_STATUS_DATE);
		ObjectDataInputField futureStatusSummaryField = createStringField(ObjectType.INDICATOR,  Indicator.TAG_FUTURE_STATUS_SUMMARY);
		ObjectDataInputField futureStatusRatingField = createRatingChoiceField(ObjectType.INDICATOR, new StatusQuestion(Indicator.TAG_FUTURE_STATUS_RATING));
		addFieldsOnOneLine(EAM.text("Desired Status"), new GoalIcon(), new ObjectDataInputField[]{futureStatusDateField, futureStatusSummaryField, futureStatusRatingField});

		addField(createMultilineField(Indicator.getObjectType(), Indicator.TAG_FUTURE_STATUS_DETAIL));
		addField(createMultilineField(Indicator.getObjectType(), Indicator.TAG_FUTURE_STATUS_COMMENT));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Desired Future Status");
	}
}
