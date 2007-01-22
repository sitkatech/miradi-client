/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.DiagramView;

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
			CommandSwitchView cmd = new CommandSwitchView(DiagramView.getViewName());
			Project project = ((ThreatRatingWizardPanel)getWizard()).getView().getProject();
			try
			{
				project.executeCommand(cmd);
			}
			catch (CommandFailedException e)
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


