/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.HtmlViewPanel;

public class ThreatRatingWizardCheckTotalsStep extends ThreatRatingWizardStep
{
	public ThreatRatingWizardCheckTotalsStep(ThreatRatingWizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals(SHOW_RULES))
		{
			MainWindow mainWindow = getWizard().getMainWindow();
			HtmlViewPanel htmlViewPanel = 
				new HtmlViewPanel(mainWindow, EAM.text("Bundle Rules"), this.getClass(), getResourceBundleRulesFileName());
			htmlViewPanel.showAsOkDialog();
		}
		else 
			super.linkClicked(linkDescription);
	}

	
	public String getResourceBundleRulesFileName()
	{
		return HTML_BUNDLE_RULES_FILENAME;
	}

	String HTML_BUNDLE_RULES_FILENAME = "ThreatRatingBundleRules.html";
	static final String SHOW_RULES = "ShowRules";
}

