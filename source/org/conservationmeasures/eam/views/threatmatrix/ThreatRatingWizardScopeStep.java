/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatRatingWizardScopeStep extends ThreatRatingWizardSetValue
{
	public static ThreatRatingWizardScopeStep create(ThreatRatingWizardPanel wizardToUse) throws Exception
	{
		ThreatRatingFramework framework = wizardToUse.getView().getProject().getThreatRatingFramework();
		int criterionId = framework.findCriterionByLabel("Scope").getId();
		ThreatRatingWizardScopeStep step = new ThreatRatingWizardScopeStep(wizardToUse, criterionId);
		return step;
	}
	
	private ThreatRatingWizardScopeStep(ThreatRatingWizardPanel wizardToUse, int criterionIdToUse) throws Exception
	{
		super(wizardToUse, criterionIdToUse);
	}

	public String getHtmlText()
	{
		return new ThreatRatingWizardScopeText(getValueOptionLabels(), value.getLabel()).getText();
	}

}
