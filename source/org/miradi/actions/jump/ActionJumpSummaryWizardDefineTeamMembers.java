/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.actions.jump;

import java.awt.event.KeyEvent;

import org.miradi.actions.AbstractJumpMenuAction;
import org.miradi.main.MainWindow;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;

public class ActionJumpSummaryWizardDefineTeamMembers extends AbstractJumpMenuAction
{
	public ActionJumpSummaryWizardDefineTeamMembers(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, OpenStandardsConceptualizeQuestion.getSelectIntialTeamMembersLabel());
	}

	@Override
	public int getMnemonic()
	{
		return KeyEvent.VK_S;
	}

	@Override
	public String getCode()
	{
		return OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE;
	}
}
