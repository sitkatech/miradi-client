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
package org.miradi.views.umbrella.doers;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;

public class DeleteLegacyTncStrategyRankingDoer extends AbstractLegacyTncRankingDoer
{
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		if (!didUserConfirm())
			return;

		BaseObject strategy = getSingleSelected(Strategy.getObjectType());
		CommandSetObjectData clearLegacyRanking = new CommandSetObjectData(strategy.getRef(), Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING, "");
		getProject().executeCommand(clearLegacyRanking);
	}
	private boolean didUserConfirm()
	{
		String[] buttons = {OK_BUTTON, CANCEL_BUTTON};
		String title = EAM.text("Delete...");
		String[] body = {EAM.text("Are you sure?")};

		return EAM.confirmDialog(title, body, buttons);
	}
	
	private final String OK_BUTTON = EAM.text("Button|Ok");
	private final String CANCEL_BUTTON = EAM.text("Button|Cancel");
}
