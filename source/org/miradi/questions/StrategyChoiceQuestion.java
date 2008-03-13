/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objects.Factor;
import org.miradi.project.Project;

public class StrategyChoiceQuestion extends ObjectQuestion
{
	public StrategyChoiceQuestion(Project project)
	{
		super(getNonDraftStrategies(project));
	}
	
	private static Factor[] getNonDraftStrategies(Project project)
	{
		return project.getStrategyPool().getNonDraftStrategies();
	}
}
