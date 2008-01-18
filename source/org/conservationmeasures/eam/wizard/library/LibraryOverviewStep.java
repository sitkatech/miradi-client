/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard.library;

import org.conservationmeasures.eam.actions.views.ActionViewImages;
import org.conservationmeasures.eam.views.library.LibraryView;
import org.conservationmeasures.eam.wizard.SplitWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class LibraryOverviewStep extends SplitWizardStep
{
	public LibraryOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, LibraryView.getViewName());
	}


	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewImages.class;
	}

}
