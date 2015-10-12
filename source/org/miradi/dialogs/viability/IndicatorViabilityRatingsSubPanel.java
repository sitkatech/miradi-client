/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.viability;

import java.awt.Font;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;

public class IndicatorViabilityRatingsSubPanel extends ObjectDataInputPanel
{
	public IndicatorViabilityRatingsSubPanel(Project projectToUse, ORef orefToUse) throws Exception
	{
		super(projectToUse, orefToUse);
		
		ChoiceQuestion statusQuestion = StaticQuestionManager.getQuestion(StatusQuestion.class);
		
		createThresholdField(statusQuestion.findChoiceByCode(StatusQuestion.POOR));
		createThresholdField(statusQuestion.findChoiceByCode(StatusQuestion.FAIR));
		createThresholdField(statusQuestion.findChoiceByCode(StatusQuestion.GOOD));
		createThresholdField(statusQuestion.findChoiceByCode(StatusQuestion.VERY_GOOD));
				
		addField(createRatingChoiceField(ObjectType.INDICATOR, Indicator.TAG_RATING_SOURCE, new RatingSourceQuestion()));
		addField(createMultilineField(ObjectType.INDICATOR, Indicator.TAG_VIABILITY_RATINGS_COMMENTS));
		
		updateFieldsFromProject();
	}

	private void createThresholdField(ChoiceItem choiceItem) throws Exception
	{
		PanelTitleLabel label = new PanelTitleLabel(choiceItem.getLabel());
		Font font = label.getFont();
		label.setFont(font.deriveFont(Font.BOLD));
		label.setOpaque(true);
		label.setBackground(choiceItem.getColor());
		
		ObjectDataInputField mapField = createStringMapField(IndicatorSchema.getObjectType(), Indicator.TAG_THRESHOLDS_MAP, choiceItem.getCode(), 10);
		ObjectDataInputField detailsField = createStringMapField(IndicatorSchema.getObjectType(), Indicator.TAG_THRESHOLD_DETAILS_MAP, choiceItem.getCode(), 30);
		addFieldsOnOneLine(label, new ObjectDataInputField[]{mapField, detailsField});
	}
	
	@Override
	protected boolean doesSectionContainFieldWithTag(String tagToUse)
	{
		if (isThresholdTag(tagToUse))
			return true;

		// apparently the rating source is mapped via the measurement status confidence tag in the upper panel
		if (isStatusConfidenceTag(tagToUse))
			return true;

		return super.doesSectionContainFieldWithTag(tagToUse);
	}
	
	private boolean isThresholdTag(String tagToUse)
	{
		if (tagToUse.equals(StatusQuestion.POOR))
			return true;
		
		if (tagToUse.equals(StatusQuestion.FAIR))
			return true;
		
		if (tagToUse.equals(StatusQuestion.GOOD))
			return true;

		if (tagToUse.equals(StatusQuestion.VERY_GOOD))
			return true;
			
		return false;
	}

	private boolean isStatusConfidenceTag(String tagToUse)
	{
		if (tagToUse.equals(Measurement.TAG_STATUS_CONFIDENCE))
			return true;

		return false;
	}

	@Override
	public boolean shouldBeEnabled()
	{
		if (getProject().isStressBaseMode())
			return true;

		ORef foundKeaRef = new ORefList(this.getSelectedRefs()).getRefForType(KeyEcologicalAttributeSchema.getObjectType());
		return foundKeaRef.isValid();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Viability Ratings");
	}
}
