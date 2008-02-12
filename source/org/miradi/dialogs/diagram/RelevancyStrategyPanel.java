/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.StrategyChoiceQuestion;

public class RelevancyStrategyPanel extends ObjectDataInputPanel
{
	public RelevancyStrategyPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addField(createStrategyRelevancyOverrideListField(new StrategyChoiceQuestion(getProject())));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Strategy Relevancy Panel");
	}
}
