/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;

public class StrategyChoiceQuestion extends ObjectQuestion
{
	public StrategyChoiceQuestion(Project project)
	{
		super(project, Strategy.getObjectType(), EAM.text("Strategy list"));
	}
}
