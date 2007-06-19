/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class ThreatRatingWizardPanel extends WizardPanel
{
	public ThreatRatingWizardPanel(ThreatMatrixView viewToUse)
	{
		super(viewToUse.getMainWindow(), viewToUse);
	}
}

