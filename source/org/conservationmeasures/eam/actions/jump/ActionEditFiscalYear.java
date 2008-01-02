/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions.jump;

import org.conservationmeasures.eam.actions.ViewAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionEditFiscalYear extends ViewAction
{
	public ActionEditFiscalYear(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, EAM.text("Action|Edit..."));
	}

}
