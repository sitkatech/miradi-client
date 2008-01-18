/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;

public class ProcessMenu4b extends MiradiMenu
{
	public ProcessMenu4b(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_4B, actions);
		
		addDisabledMenuItem(EAM.text("Analyze project results and assumptions"));
		addDisabledMenuItem(EAM.text("Analyze operational and financial data"));
		addDisabledMenuItem(EAM.text("Document discussions and decisions"));
		
	}
}
