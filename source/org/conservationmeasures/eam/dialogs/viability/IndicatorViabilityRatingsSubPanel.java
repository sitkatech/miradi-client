/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.Color;
import java.awt.Font;

import org.conservationmeasures.eam.dialogfields.ViabilityRatingsTableField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.RatingSourceQuestion;

public class IndicatorViabilityRatingsSubPanel extends ObjectDataInputPanel
{
	public IndicatorViabilityRatingsSubPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		createThreshholdField("1", ChoiceQuestion.COLOR_ALERT, "Poor");
		createThreshholdField("2", ChoiceQuestion.COLOR_CAUTION, "Fair");
		createThreshholdField("3", ChoiceQuestion.COLOR_OK, "Good");
		createThreshholdField("4", ChoiceQuestion.COLOR_GREAT, "Very Good");
				
		addField(createRatingChoiceField(ObjectType.INDICATOR, Indicator.TAG_RATING_SOURCE, new RatingSourceQuestion()));
		addField(createMultilineField(ObjectType.INDICATOR, Indicator.TAG_VIABILITY_RATINGS_COMMENT));
		
		updateFieldsFromProject();
	}

	private void createThreshholdField(String code, Color color, String labelToUse)
	{
		PanelTitleLabel label = new PanelTitleLabel(EAM.text(labelToUse));
		Font font = label.getFont();
		label.setFont(font.deriveFont(Font.BOLD));
		label.setOpaque(true);
		label.setBackground(color);
		addFieldWithCustomLabel(createStringMapField(Indicator.getObjectType(), Indicator.TAG_INDICATOR_THRESHOLD, code), label);
	}

	//FIXME remove field and class
	public ViabilityRatingsTableField createViabilityRatingsTableField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new ViabilityRatingsTableField(getProject(), objectType, getObjectIdForType(objectType), tagToUse, question);
	}	

	public String getPanelDescription()
	{
		return EAM.text("Viability Ratings");
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		final boolean areIndicatorMeasurementFields = 
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.MEASUREMENT,Measurement.TAG_SUMMARY) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.MEASUREMENT,Measurement.TAG_STATUS) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.MEASUREMENT,Measurement.TAG_TREND) ||
			
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_FUTURE_STATUS_SUMMARY) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_FUTURE_STATUS_RATING);
		
		if (areIndicatorMeasurementFields)
		{
			ratingThresholdTable.dataHasChanged();
		}
	}
	
	private ViabilityRatingsTableField ratingThresholdTable;
}
