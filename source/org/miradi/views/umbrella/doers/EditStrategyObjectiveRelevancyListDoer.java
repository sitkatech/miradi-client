/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.RelevancyObjectiveOverridePanel;
import org.miradi.main.EAM;
import org.miradi.schemas.StrategySchema;

public class EditStrategyObjectiveRelevancyListDoer extends	AbstractEditListDoer
{
	@Override
	protected int getObjectType()
	{
		return StrategySchema.getObjectType();
	}

	@Override
	protected DisposablePanel createEditPanel() throws Exception
	{
		return new RelevancyObjectiveOverridePanel(getProject(), getSelectedRef(), EAM.text("Select the Objectives which this Strategy helps to achieve"));
	}

	@Override
	protected String getDialogTitle()
	{
		return EAM.text("Choose Relevant Objective(s)");
	}
	
	@Override
	protected boolean shouldHaveScrollBars()
	{
		return true;
	}
}
