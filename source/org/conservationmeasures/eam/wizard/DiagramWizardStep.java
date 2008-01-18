/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard;

import org.conservationmeasures.eam.views.diagram.DiagramView;

public class DiagramWizardStep extends SplitWizardStep
{

	public DiagramWizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, DiagramView.getViewName());
	}

}
