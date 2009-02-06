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
package org.miradi.dialogs.viability;

import java.awt.Font;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StatusQuestion;

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
		PanelTitleLabel label = new PanelTitleLabel(choiceItem.getLabel());
		Font font = label.getFont();
		label.setFont(font.deriveFont(Font.BOLD));
		label.setOpaque(true);
		label.setBackground(choiceItem.getColor());
		
		ObjectDataInputField mapField = createStringMapField(Indicator.getObjectType(), Indicator.TAG_INDICATOR_THRESHOLD, choiceItem.getCode(), 10);
		ObjectDataInputField detailsField = createStringMapField(Indicator.getObjectType(), Indicator.TAG_THRESHOLD_DETAILS, choiceItem.getCode(), 30);
		addFieldsOnOneLine(label, new ObjectDataInputField[]{mapField, detailsField});
	}

	public String getPanelDescription()
	{
		return EAM.text("Viability Ratings");
	}
}
