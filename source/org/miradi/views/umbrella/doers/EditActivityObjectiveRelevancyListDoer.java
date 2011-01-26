/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.RelevancyObjectivePanel;
import org.miradi.main.EAM;
import org.miradi.objects.Task;

public class EditActivityObjectiveRelevancyListDoer extends	AbstractEditListDoer
{
	@Override
	protected int getObjectType()
	{
		return Task.getObjectType();
	}

	@Override
	protected DisposablePanel createEditPanel() throws Exception
	{
		return new RelevancyObjectivePanel(getProject(), getSelectedRef());
	}

	@Override
	protected String getDialogTitle()
	{
		return EAM.text("Choose Relevant Objective(s)");
	}
}
