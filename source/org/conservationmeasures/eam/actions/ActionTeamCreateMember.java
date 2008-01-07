/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionTeamCreateMember extends ObjectsAction
{
		public ActionTeamCreateMember(MainWindow mainWindowToUse)
		{
			super(mainWindowToUse, getLabel());
		}

		private static String getLabel()
		{
			return EAM.text("Action|Manage|Create Member");
		}

		public String getToolTipText()
		{
			return EAM.text("TT|Add member to the team");
		}
		
	}

