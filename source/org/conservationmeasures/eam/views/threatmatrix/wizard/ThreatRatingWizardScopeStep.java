/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatRatingWizardScopeStep extends ThreatRatingWizardSetValue
{
	public static ThreatRatingWizardScopeStep create(ThreatRatingWizardPanel wizardToUse) throws Exception
	{
		ThreatRatingFramework framework = wizardToUse.getView().getProject().getThreatRatingFramework();
		BaseId criterionId = framework.findCriterionByLabel("Scope").getId();
		ThreatRatingWizardScopeStep step = new ThreatRatingWizardScopeStep(wizardToUse, criterionId);
		return step;
	}
	
	private ThreatRatingWizardScopeStep(ThreatRatingWizardPanel wizardToUse, BaseId criterionIdToUse) throws Exception
	{
		super(wizardToUse, criterionIdToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	String HTML_FILENAME = "ThreatRatingScope.html";

}
