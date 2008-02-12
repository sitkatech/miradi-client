/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions.jump;

import org.miradi.actions.MainWindowAction;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionJumpDiagramWizardConstructChainsStep extends
		MainWindowAction
{
	public ActionJumpDiagramWizardConstructChainsStep(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	static String getLabel()
	{
		return EAM.text("Create initial conceptual model");
	}

}
