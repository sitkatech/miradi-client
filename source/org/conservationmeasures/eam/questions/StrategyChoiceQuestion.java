/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;

public class StrategyChoiceQuestion extends ObjectQuestion
{
	public StrategyChoiceQuestion(Project project)
	{
		super(project, getNonDraftStrategies(project));
	}
	
	private static Factor[] getNonDraftStrategies(Project project)
	{
		return project.getStrategyPool().getNonDraftStrategies();
	}
}
