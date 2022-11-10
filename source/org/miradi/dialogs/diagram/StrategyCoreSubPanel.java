/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.diagram;

import org.miradi.actions.ActionEditStrategyGoalRelevancyList;
import org.miradi.actions.ActionEditStrategyIndicatorRelevancyList;
import org.miradi.actions.ActionEditStrategyObjectiveRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.StrategySchema;


public class StrategyCoreSubPanel extends ObjectDataInputPanel
{
	public StrategyCoreSubPanel(Project projectToUse, Actions actions, int objectType) throws Exception
	{
		super(projectToUse, objectType);

		ObjectDataInputField shortLabelField = createStringField(StrategySchema.getObjectType(), Strategy.TAG_SHORT_LABEL,10);
		ObjectDataInputField labelField = createExpandableField(StrategySchema.getObjectType(), Strategy.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Strategy"), IconManager.getStrategyIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});
		addField(createMultilineField(StrategySchema.getObjectType(), Factor.TAG_TEXT));

		addTaxonomyFields(StrategySchema.getObjectType());
		addField(createRadioButtonEditorFieldWithHierarchies(StrategySchema.getObjectType(), Strategy.TAG_STANDARD_CLASSIFICATION_V11_CODE, new StrategyClassificationQuestionV11()));
		addField(createRadioButtonEditorFieldWithHierarchies(StrategySchema.getObjectType(), Strategy.TAG_STANDARD_CLASSIFICATION_V20_CODE, new StrategyClassificationQuestionV20()));

		ObjectDataInputField impactField = createRadioButtonEditorField(StrategySchema.getObjectType(), Strategy.TAG_IMPACT_RATING, getQuestion(StrategyImpactQuestion.class));
		ObjectDataInputField feasibilityField = createRadioButtonEditorField(StrategySchema.getObjectType(), Strategy.TAG_FEASIBILITY_RATING, getQuestion(StrategyFeasibilityQuestion.class));
		ObjectDataInputField prioritySummaryField = createReadOnlyChoiceField(Strategy.PSEUDO_TAG_RATING_SUMMARY, getQuestion(StrategyRatingSummaryQuestion.class));
		addFieldsOnOneLine(EAM.text("Rating"), new ObjectDataInputField[] {impactField, feasibilityField, prioritySummaryField});

		if (LegacyTncStrategyRankingEditorPropertiesSubPanel.hasLegacyTncRankings(getProject(), getStrategyRef()))
			addLabeledSubPanelWithoutBorder(new LegacyTncStrategyRankingEditorPropertiesSubPanel(getProject(), getRefForType(StrategySchema.getObjectType()), actions), EAM.text("Legacy TNC Ratings"));
		
		addFieldWithEditButton(EAM.text("Objectives"), createReadOnlyObjectList(StrategySchema.getObjectType(), Strategy.PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS), createObjectsActionButton(actions.getObjectsAction(ActionEditStrategyObjectiveRelevancyList.class), getPicker()));
		addFieldWithEditButton(EAM.text("Goals"), createReadOnlyObjectList(StrategySchema.getObjectType(), Strategy.PSEUDO_TAG_RELEVANT_GOAL_REFS), createObjectsActionButton(actions.getObjectsAction(ActionEditStrategyGoalRelevancyList.class), getPicker()));
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(StrategySchema.getObjectType(), Strategy.PSEUDO_TAG_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actions.getObjectsAction(ActionEditStrategyIndicatorRelevancyList.class), getPicker()));
	}

	@Override
	protected boolean doesSectionContainFieldWithTag(String tagToUse)
	{
		if (tagToUse.equals(Factor.PSEUDO_TAG_TAXONOMY_CODE_VALUE))
			return true;

		if (tagToUse.equals(CustomPlanningColumnsQuestion.META_CURRENT_RATING))
			return true;

		return super.doesSectionContainFieldWithTag(tagToUse);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}

	private ORef getStrategyRef()
	{
		return getSelectedRefs().getRefForType(StrategySchema.getObjectType());
	}
}
