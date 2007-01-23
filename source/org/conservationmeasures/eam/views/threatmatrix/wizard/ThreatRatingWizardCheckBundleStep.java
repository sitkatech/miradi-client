/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.HtmlViewPanel;

public class ThreatRatingWizardCheckBundleStep extends ThreatRatingWizardStep
{
	public ThreatRatingWizardCheckBundleStep(ThreatRatingWizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	

	public void buttonPressed(String buttonName)
	{
		if(buttonName.equals("Next"))
		{
			try
			{
				getThreatRatingWizard().jump(ThreatRatingWizardChooseBundle.class);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
			return;
		}
		super.buttonPressed(buttonName);
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals(SHOW_RULES))
		{
			HtmlViewPanel htmlViewPanel = new HtmlViewPanel("Threat Calcualtions", this.getClass(), getResourceBundleRulesFileName());
			htmlViewPanel.showOkDialog();
		}
		else 
			super.linkClicked(linkDescription);
	}

	public String getResourceBundleRulesFileName()
	{
		return HTML_EXPLANATION_OF_CALCULATION_FILENAME;
	}
	
	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	String HTML_EXPLANATION_OF_CALCULATION_FILENAME = "ThreatRatingExplanationOfCalculation.html";
	String HTML_FILENAME = "ThreatRatingCheckBundle.html";
	
	static final String SHOW_RULES = "ShowRules";

}

