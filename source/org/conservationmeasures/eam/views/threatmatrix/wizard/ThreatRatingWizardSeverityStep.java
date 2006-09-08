/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatRatingWizardSeverityStep extends ThreatRatingWizardSetValue
{
	public static ThreatRatingWizardSeverityStep create(ThreatRatingWizardPanel wizardToUse) throws Exception
	{
		ThreatRatingFramework framework = wizardToUse.getView().getProject().getThreatRatingFramework();
		BaseId criterionId = framework.findCriterionByLabel("Severity").getId();
		ThreatRatingWizardSeverityStep step = new ThreatRatingWizardSeverityStep(wizardToUse, criterionId);
		return step;
	}
	
	private ThreatRatingWizardSeverityStep(ThreatRatingWizardPanel wizardToUse, BaseId criterionIdToUse) throws Exception
	{
		super(wizardToUse, criterionIdToUse);
	}

	public String getText()
	{
		return new ThreatRatingWizardSeverityText(getValueOptionLabels(), value.getLabel()).getText();
	}
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:Severity"))
			EAM.okDialog("Definition: Severity", new String[] {"Severity is..."});
	}


}
