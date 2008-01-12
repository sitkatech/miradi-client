/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class IndicatorChoiceQuestion extends ObjectQuestion
{
	public IndicatorChoiceQuestion(Project project)
	{
		super(project, getAllIndicators(project), EAM.text("Indicator list"));
	}
	
	private static Indicator[] getAllIndicators(Project project)
	{
		return project.getIndicatorPool().getAllIndicators();
	}

}
