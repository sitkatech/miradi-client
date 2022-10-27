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

import org.miradi.actions.*;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.*;
import org.miradi.utils.ObjectsActionButton;

public class FactorSummaryCorePanel extends ObjectDataInputPanel
{
	public FactorSummaryCorePanel(Project project, Actions actions, DiagramFactor diagramFactorToEdit) throws Exception
	{
		super(project, diagramFactorToEdit.getRef());
		
		Factor factorToEdit = diagramFactorToEdit.getWrappedFactor();
		setObjectRefs(new ORef[] {factorToEdit.getRef(), diagramFactorToEdit.getRef(),});

		ObjectDataInputField shortLabelField = createShortStringField(Factor.TAG_SHORT_LABEL);
		labelField = createExpandableField(Factor.TAG_LABEL);
		
		addFieldsOnOneLine(EAM.fieldLabel(factorToEdit.getType(), factorToEdit.getTypeName()), 
				IconManager.getImage(factorToEdit), 
				new ObjectDataInputField[]{shortLabelField, labelField});
		addField(createMultilineField(Factor.TAG_TEXT));

		ObjectDataInputField fontField = createChoiceField(DiagramFactorSchema.getObjectType(), DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion());
		ObjectDataInputField colorField =  createColorChoiceField(DiagramFactorSchema.getObjectType(), DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion());
		ObjectDataInputField styleField = createChoiceField(DiagramFactorSchema.getObjectType(), DiagramFactor.TAG_FONT_STYLE, new DiagramFactorFontStyleQuestion());
		addFieldsOnOneLine(EAM.text("Font"), new ObjectDataInputField[]{fontField, colorField, styleField});
		
		if (factorToEdit.isBiophysicalFactor())
		{
			addTaxonomyFields(BiophysicalFactorSchema.getObjectType());
		}
		if (factorToEdit.isBiophysicalResult())
		{
			addTaxonomyFields(BiophysicalResultSchema.getObjectType());
		}
		if (factorToEdit.isCause())
		{
			addField(createCheckBoxField(CauseSchema.getObjectType(), Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));
			addTaxonomyFields(CauseSchema.getObjectType());
		}
		if (factorToEdit.isDirectThreat())
		{
			addField(createRadioButtonEditorFieldWithHierarchies(CauseSchema.getObjectType(), Cause.TAG_STANDARD_CLASSIFICATION_V11_CODE, new ThreatClassificationQuestionV11()));
			addField(createRadioButtonEditorFieldWithHierarchies(CauseSchema.getObjectType(), Cause.TAG_STANDARD_CLASSIFICATION_V20_CODE, new ThreatClassificationQuestionV20()));
		}
		if (factorToEdit.isIntermediateResult())
		{
			addTaxonomyFields(IntermediateResultSchema.getObjectType());
		}

		if(factorToEdit.isStrategy())
		{
			addOptionalDraftStatusCheckBox(Strategy.TAG_STATUS);
			addField(createRadioButtonEditorFieldWithHierarchies(StrategySchema.getObjectType(), Strategy.TAG_STANDARD_CLASSIFICATION_V11_CODE, new StrategyClassificationQuestionV11()));
			addField(createRadioButtonEditorFieldWithHierarchies(StrategySchema.getObjectType(), Strategy.TAG_STANDARD_CLASSIFICATION_V20_CODE, new StrategyClassificationQuestionV20()));
			ObjectDataInputField impactField = createRadioButtonEditorField(StrategySchema.getObjectType(), Strategy.TAG_IMPACT_RATING, getQuestion(StrategyImpactQuestion.class));
			ObjectDataInputField feasibilityField = createRadioButtonEditorField(StrategySchema.getObjectType(), Strategy.TAG_FEASIBILITY_RATING, getQuestion(StrategyFeasibilityQuestion.class));
			ObjectDataInputField prioritySummaryField = createReadOnlyChoiceField(Strategy.PSEUDO_TAG_RATING_SUMMARY, getQuestion(StrategyRatingSummaryQuestion.class));
			addFieldsOnOneLine(EAM.text("Rating"), new ObjectDataInputField[] {impactField, feasibilityField, prioritySummaryField});
			addLabeledSubPanelWithoutBorder(new LegacyTncStrategyRankingEditorPropertiesSubPanel(getProject(), factorToEdit.getRef(), actions), EAM.text("Legacy TNC Ratings"));
			addFieldWithEditButton(EAM.text("Objectives"), createReadOnlyObjectList(StrategySchema.getObjectType(), Strategy.PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS), createObjectsActionButton(actions.getObjectsAction(ActionEditStrategyObjectiveRelevancyList.class), getPicker()));
			addFieldWithEditButton(EAM.text("Goals"), createReadOnlyObjectList(StrategySchema.getObjectType(), Strategy.PSEUDO_TAG_RELEVANT_GOAL_REFS), createObjectsActionButton(actions.getObjectsAction(ActionEditStrategyGoalRelevancyList.class), getPicker()));
			addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(StrategySchema.getObjectType(), Strategy.PSEUDO_TAG_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actions.getObjectsAction(ActionEditStrategyIndicatorRelevancyList.class), getPicker()));
			addTaxonomyFields(StrategySchema.getObjectType());
		}

		if(factorToEdit.isTarget())
		{
			addField(createStringField(Target.TAG_SPECIES_LATIN_NAME));
			addField(createQuestionFieldWithDescriptionPanel(TargetSchema.getObjectType(), Target.TAG_HABITAT_ASSOCIATION, new HabitatAssociationQuestion()));
			addTaxonomyFields(TargetSchema.getObjectType());
		}
		if (factorToEdit.isHumanWelfareTarget())
		{
			addTaxonomyFields(HumanWelfareTargetSchema.getObjectType());
		}
		if (factorToEdit.isAnalyticalQuestion())
		{
			addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(AnalyticalQuestionSchema.getObjectType(), AnalyticalQuestion.PSEUDO_TAG_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actions.getObjectsAction(ActionEditAnalyticalQuestionIndicatorRelevancyList.class), getPicker()));

	        addField(createMultilineField(AnalyticalQuestionSchema.getObjectType(), AbstractAnalyticalQuestion.TAG_COMMENTS));
			addField(createRadioButtonEditorField(AnalyticalQuestionSchema.getObjectType(), BaseObject.TAG_EVIDENCE_CONFIDENCE, EvidenceConfidenceTypeQuestion.getQuestion(AnalyticalQuestionSchema.getObjectType())));
			addField(createMultilineField(AnalyticalQuestionSchema.getObjectType(), BaseObject.TAG_EVIDENCE_NOTES));
	        addField(createMultilineField(AnalyticalQuestionSchema.getObjectType(), AbstractAnalyticalQuestion.TAG_IMPLICATIONS));
	        addField(createMultilineField(AnalyticalQuestionSchema.getObjectType(), AbstractAnalyticalQuestion.TAG_FUTURE_INFORMATION_NEEDS));
			addTaxonomyFields(AnalyticalQuestionSchema.getObjectType());
		}

		addField(createReadOnlyObjectList(factorToEdit.getType(), Factor.PSEUDO_TAG_CONCEPTUAL_DIAGRAM_REFS));
		addField(createReadOnlyObjectList(factorToEdit.getType(), Factor.PSEUDO_TAG_RESULTS_CHAIN_REFS));

		ObjectsActionButton chooseTagForFactorButton = createObjectsActionButton(actions.getObjectsAction(ActionManageFactorTags.class), getPicker());
		ObjectDataInputField readOnlyTaggedObjects = createReadOnlyObjectList(diagramFactorToEdit.getRef().getObjectType(), DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS);
		addFieldWithEditButton(getTagsLabel(), readOnlyTaggedObjects, chooseTagForFactorButton);
		
		if (factorToEdit.isThreatReductionResult())
		{
			addTaxonomyFields(ThreatReductionResultSchema.getObjectType());
		}
	}

	@Override
	public void becomeActive()
	{
		super.becomeActive();
		updateFieldsFromProject();
	}

	public static String getTagsLabel()
	{
		return EAM.text("Tags");
	}

	private void addOptionalDraftStatusCheckBox(String tag)
	{
		if (!getProject().isChainMode())
			return;
		
		ObjectDataInputField field = createCheckBoxField(tag, StrategyStatusQuestion.STATUS_DRAFT_CODE, StrategyStatusQuestion.STATUS_REAL_CODE);
		addField(field);
	}

	@Override
	public void setFocusOnFirstField()
	{
		labelField.getComponent().requestFocusInWindow();
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}

	private ObjectDataInputField labelField;
}
