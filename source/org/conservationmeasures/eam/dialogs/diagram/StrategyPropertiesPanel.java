/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.BudgetCostModeQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;
import org.conservationmeasures.eam.questions.StrategyTaxonomyQuestion;

public class StrategyPropertiesPanel extends ObjectDataInputPanel
{
	public StrategyPropertiesPanel(Project projectToUse)
	{
		super(projectToUse, Strategy.getObjectType(), BaseId.INVALID);
		
		addField(createStringField(Strategy.TAG_SHORT_LABEL));
		addField(createStringField(Strategy.TAG_LABEL));
		addField(createRatingChoiceField(new StrategyImpactQuestion(Strategy.TAG_IMPACT_RATING)));
		addField(createRatingChoiceField(new StrategyFeasibilityQuestion(Strategy.TAG_FEASIBILITY_RATING)));
		addField(createReadOnlyChoiceField(new StrategyRatingSummaryQuestion(Strategy.PSEUDO_TAG_RATING_SUMMARY)));
		addField(createReadOnlyChoiceField(new StrategyTaxonomyQuestion(Strategy.TAG_TAXONOMY_CODE)));
		
		addField(createChoiceField(Strategy.getObjectType(), new BudgetCostModeQuestion(Strategy.TAG_BUDGET_COST_MODE)));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_BUDGET_COST_ROLLUP));
		addField(createStringField(Strategy.getObjectType(), Strategy.TAG_BUDGET_COST_OVERRIDE));
		
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
