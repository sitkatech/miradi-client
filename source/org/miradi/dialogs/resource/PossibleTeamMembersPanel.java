/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.resource;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

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
