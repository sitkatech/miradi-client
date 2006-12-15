/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StrategyCostQuestion;
import org.conservationmeasures.eam.questions.StrategyDurationQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummary;
import org.conservationmeasures.eam.questions.StrategyTaxonomyQuestion;

public class StrategyPropertiesPanel extends ObjectDataInputPanel
{
	public StrategyPropertiesPanel(Project projectToUse, int objectTypeToUse, BaseId idToShow)
	{
		super(projectToUse, objectTypeToUse, idToShow);
		addField(createStringField(Strategy.TAG_SHORT_LABEL));
		addField(createStringField(Strategy.TAG_LABEL));
		addField(createChoiceField(new StrategyImpactQuestion(Strategy.TAG_IMPACT_RATING)));
		addField(createChoiceField(new StrategyDurationQuestion(Strategy.TAG_DURATION_RATING)));
		addField(createChoiceField(new StrategyFeasibilityQuestion(Strategy.TAG_FEASIBILITY_RATING)));
		addField(createChoiceField(new StrategyCostQuestion(Strategy.TAG_COST_RATING)));
		addField(createReadOnlyChoiceField(new StrategyRatingSummary(Strategy.PSEUDO_TAG_RATING_SUMMARY)));
		addField(createReadOnlyChoiceField(new StrategyTaxonomyQuestion(Strategy.TAG_TAXONOMY_CODE)));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_GOALS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_OBJECTIVES));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_DIRECT_THREATS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_TARGETS));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Strategy Properties");
	}

}
