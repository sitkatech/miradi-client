/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.IndicatorChoiceQuestion;

public class RelevancyIndicatorPanel extends ObjectDataInputPanel
{
	public RelevancyIndicatorPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addField(createRelevancyOverrideListField(new IndicatorChoiceQuestion(getProject()), Objective.PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS, Objective.TAG_RELEVANT_INDICATOR_SET));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Indicator Relevancy Panel");
	}
}
