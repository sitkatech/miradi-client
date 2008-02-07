/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.Font;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.RatingSourceQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class IndicatorViabilityRatingsSubPanel extends ObjectDataInputPanel
{
	public IndicatorViabilityRatingsSubPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		ChoiceQuestion statusQuestion = getProject().getQuestion(StatusQuestion.class);
		
		createThreshholdField(statusQuestion.findChoiceByCode(StatusQuestion.POOR));
		createThreshholdField(statusQuestion.findChoiceByCode(StatusQuestion.FAIR));
		createThreshholdField(statusQuestion.findChoiceByCode(StatusQuestion.GOOD));
		createThreshholdField(statusQuestion.findChoiceByCode(StatusQuestion.VERY_GOOD));
				
		addField(createRatingChoiceField(ObjectType.INDICATOR, Indicator.TAG_RATING_SOURCE, new RatingSourceQuestion()));
		addField(createMultilineField(ObjectType.INDICATOR, Indicator.TAG_VIABILITY_RATINGS_COMMENT));
		
		updateFieldsFromProject();
	}

	private void createThreshholdField(ChoiceItem choiceItem)
	{
		PanelTitleLabel label = new PanelTitleLabel(EAM.text(choiceItem.getLabel()));
		Font font = label.getFont();
		label.setFont(font.deriveFont(Font.BOLD));
		label.setOpaque(true);
		label.setBackground(choiceItem.getColor());
		
		ObjectDataInputField detailsField = createStringMapField(Indicator.getObjectType(), Indicator.TAG_THRESHOLD_DETAILS, choiceItem.getCode());
		ObjectDataInputField mapField = createStringMapField(Indicator.getObjectType(), Indicator.TAG_INDICATOR_THRESHOLD, choiceItem.getCode());
		addFieldsOnOneLine(label, new ObjectDataInputField[]{mapField, detailsField});
	}

	public String getPanelDescription()
	{
		return EAM.text("Viability Ratings");
	}
}
