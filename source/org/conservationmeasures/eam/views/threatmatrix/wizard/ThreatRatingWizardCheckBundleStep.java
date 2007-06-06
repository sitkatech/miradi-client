/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlViewPanel;

public class ThreatRatingWizardCheckBundleStep extends ThreatRatingWizardStep
{
	public ThreatRatingWizardCheckBundleStep(ThreatRatingWizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals(SHOW_RULES))
		{
			HtmlViewPanel htmlViewPanel = 
				new HtmlViewPanel(getMainWindow(), EAM.text("Threat Calcualtions"), this.getClass(), getResourceBundleRulesFileName());
			htmlViewPanel.showAsOkDialog();
		}
		else 
			super.linkClicked(linkDescription);
	}

	public String getResourceBundleRulesFileName()
	{
		return HTML_EXPLANATION_OF_CALCULATION_FILENAME;
	}

	String HTML_EXPLANATION_OF_CALCULATION_FILENAME = "ThreatRatingExplanationOfCalculation.html";
	
	static final String SHOW_RULES = "ShowRules";
}

