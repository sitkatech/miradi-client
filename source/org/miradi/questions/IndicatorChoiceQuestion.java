/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objects.Indicator;
import org.miradi.project.Project;

public class IndicatorChoiceQuestion extends ObjectQuestion
{
	public IndicatorChoiceQuestion(Project project)
	{
		super(getAllIndicators(project));
	}
	
	private static Indicator[] getAllIndicators(Project project)
	{
		return project.getIndicatorPool().getAllIndicators();
	}

}
