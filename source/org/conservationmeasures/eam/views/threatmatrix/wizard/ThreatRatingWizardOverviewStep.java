/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.NewWizardPanel;

public class ThreatRatingWizardOverviewStep extends ThreatRatingWizardStep
{
	public ThreatRatingWizardOverviewStep(ThreatRatingWizardPanel wizardToUse) 
	{
		super(wizardToUse);
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("View:Diagram"))
		{
			try
			{
				((NewWizardPanel)getWizard()).control(linkDescription);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
		else 
			super.linkClicked(linkDescription);
	}
	
	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	String HTML_FILENAME = "ThreatRatingOverview.html";


}


