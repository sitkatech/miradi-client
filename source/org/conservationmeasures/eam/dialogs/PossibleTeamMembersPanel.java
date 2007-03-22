/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class PossibleTeamMembersPanel extends ResourcePoolManagementPanel
{
	public PossibleTeamMembersPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), mainWindowToUse, mainWindowToUse.getActions(), OVERVIEW_TEXT);
	}
	
	public String getSplitterDescription()
	{
		return getPanelDescription() + SPLITTER_TAG;
	}

	final static String OVERVIEW_TEXT = 
		EAM.text("<html>" +
				"<p>" +
				"This table lists all the Resources that have been created within this project. " +
				"</p>" +
				"<p>" +
				"You can select existing resources and add them to the team, " +
				"or you can create new resources." +
				"</p>" +
				"</html");

}
