/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.project.Project;

public class ThreatRatingWizardScopeStep extends ThreatRatingWizardSetValue
{
	public static ThreatRatingWizardScopeStep create(ThreatRatingWizardPanel wizardToUse, Project project) throws Exception
	{
		return  new ThreatRatingWizardScopeStep(wizardToUse,"Scope");
	}
	
	private ThreatRatingWizardScopeStep(ThreatRatingWizardPanel wizardToUse, String critertion) throws Exception
	{
		super(wizardToUse, critertion);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	String HTML_FILENAME = "ThreatRatingScope.html";

}
