/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatRatingWizardSeverityStep extends ThreatRatingWizardSetValue
{
	public static ThreatRatingWizardSeverityStep create(ThreatRatingWizardPanel wizardToUse) throws Exception
	{
		ThreatRatingFramework framework = wizardToUse.getView().getProject().getThreatRatingFramework();
		int criterionId = framework.findCriterionByLabel("Severity").getId();
		ThreatRatingWizardSeverityStep step = new ThreatRatingWizardSeverityStep(wizardToUse, criterionId);
		return step;
	}
	
	private ThreatRatingWizardSeverityStep(ThreatRatingWizardPanel wizardToUse, int criterionIdToUse) throws Exception
	{
		super(wizardToUse, criterionIdToUse);
	}

	public String getHtmlText()
	{
		return new ThreatRatingWizardSeverityText(getValueOptionLabels(), value.getLabel()).getText();
	}

}
