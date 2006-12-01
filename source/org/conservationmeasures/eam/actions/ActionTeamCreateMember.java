package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionTeamCreateMember extends ViewAction
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

