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
import org.conservationmeasures.eam.questions.StrategyTaxonomyQuestion;

public class StrategyPropertiesPanel extends ObjectDataInputPanel
{
	public StrategyPropertiesPanel(Project projectToUse, int objectTypeToUse, BaseId idToShow)
	{
		super(projectToUse, objectTypeToUse, idToShow);
		addField(createStringField(Strategy.TAG_SHORT_LABEL));
		addField(createStringField(Strategy.TAG_LABEL));
		addField(createReadOnlyChoiceField(new StrategyTaxonomyQuestion(Strategy.TAG_TAXONOMY_CODE)));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Strategy Properties");
	}

}
