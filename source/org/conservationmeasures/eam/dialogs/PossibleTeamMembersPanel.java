/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class PossibleTeamMembersPanel extends ResourcePoolManagementPanel
{
	public PossibleTeamMembersPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), mainWindowToUse.getActions(), OVERVIEW_TEXT);
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
