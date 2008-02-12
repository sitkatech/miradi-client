/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.wizard.library;

import org.miradi.actions.views.ActionViewImages;
import org.miradi.views.library.LibraryView;
import org.miradi.wizard.SplitWizardStep;
import org.miradi.wizard.WizardPanel;

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
