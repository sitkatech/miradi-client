/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelFieldLabel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
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
		PanelTitleLabel futureStatusDateLabelField = new PanelFieldLabel(futureStatusDateField.getObjectType(), futureStatusDateField.getTag());
		
		ObjectDataInputField futureStatusSummaryField = createStringField(ObjectType.INDICATOR,  Indicator.TAG_FUTURE_STATUS_SUMMARY);
		PanelTitleLabel futureStatusSummaryLabelField = new PanelFieldLabel(futureStatusSummaryField.getObjectType(), futureStatusSummaryField.getTag());

		futureStatusRatingField = createRatingChoiceField(ObjectType.INDICATOR, new StatusQuestion(Indicator.TAG_FUTURE_STATUS_RATING));
		futureStatusRatingLabelField = new PanelFieldLabel(futureStatusRatingField.getObjectType(), futureStatusRatingField.getTag());
		
		addFieldsOnOneLine(EAM.text("Desired Status"), new Object[]{futureStatusDateField, futureStatusDateLabelField, 
																	futureStatusSummaryField, futureStatusSummaryLabelField, 
																	futureStatusRatingField, futureStatusRatingLabelField});

		addField(createMultilineField(Indicator.getObjectType(), Indicator.TAG_FUTURE_STATUS_DETAIL));
		addField(createMultilineField(Indicator.getObjectType(), Indicator.TAG_FUTURE_STATUS_COMMENT));
		
		updateFieldsFromProject();
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
	
		boolean isVisible = true;
		ORef foundRef = new ORefList(orefsToUse).getRefForType(KeyEcologicalAttribute.getObjectType());
		if (foundRef.isInvalid())
			isVisible = false;
			
		setVisibilityOfRatingField(isVisible);
	}

	private void setVisibilityOfRatingField(boolean isVisible)
	{
		futureStatusRatingLabelField.setVisible(isVisible);
		futureStatusRatingField.setVisible(isVisible);
	}

	public String getPanelDescription()
	{
		return EAM.text("Desired Future Status");
	}
	
	private ObjectDataInputField futureStatusRatingField;
	private PanelTitleLabel futureStatusRatingLabelField ;
}
