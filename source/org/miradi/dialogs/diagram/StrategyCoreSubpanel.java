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
package org.miradi.dialogs.diagram;

import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;


public class StrategyCoreSubpanel extends ObjectDataInputPanel
{
	public StrategyCoreSubpanel(Project projectToUse, Actions actions, int objectType) throws Exception
	{
		super(projectToUse, objectType);

		ObjectDataInputField shortLabelField = createStringField(Strategy.getObjectType(), Strategy.TAG_SHORT_LABEL,10);
		ObjectDataInputField labelField = createExpandableField(Strategy.getObjectType(), Strategy.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Strategy"), IconManager.getStrategyIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});
		addField(createMultilineField(Strategy.getObjectType(), Factor.TAG_TEXT));

		addField(createChoiceField(Strategy.getObjectType(), Strategy.TAG_TAXONOMY_CODE, new StrategyTaxonomyQuestion()));
		
		ObjectDataInputField impactField = createRadioButtonEditorField(Strategy.getObjectType(), Strategy.TAG_IMPACT_RATING, getQuestion(StrategyImpactQuestion.class));
		ObjectDataInputField feasibilityField = createRadioButtonEditorField(Strategy.getObjectType(), Strategy.TAG_FEASIBILITY_RATING, getQuestion(StrategyFeasibilityQuestion.class));
		ObjectDataInputField prioritySummaryField = createReadOnlyChoiceField(Strategy.PSEUDO_TAG_RATING_SUMMARY, getQuestion(StrategyRatingSummaryQuestion.class));
		addFieldsOnOneLine(EAM.text("Priority"), new ObjectDataInputField[] {impactField, feasibilityField, prioritySummaryField});
		
		addLabeledSubPanelWithoutBorder(new LegacyTncStrategyRankingEditorPropertiesSubPanel(getProject(), getRefForType(Strategy.getObjectType()), actions), EAM.text("Legacy TNC Ratings"));
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}

}
