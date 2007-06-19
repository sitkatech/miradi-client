/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class ThreatRatingWizardPanel extends WizardPanel
{
	public ThreatRatingWizardPanel(ThreatMatrixView viewToUse)
	{
		super(viewToUse.getMainWindow(), viewToUse);
		view = viewToUse;
		try
		{
			selectBundle(null);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		selectedBundle = bundle;
		refresh();
	}

	public ThreatRatingBundle getSelectedBundle() throws Exception
	{
		return selectedBundle;
	}
	
	ThreatRatingBundle selectedBundle;

}

