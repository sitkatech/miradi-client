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

package org.miradi.dialogfields;

import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTablePanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.TableSettings;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractWorkPlanStringMapEditorDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		boolean superIsAvailable = super.isAvailable();
		if (!superIsAvailable)
			return false;
		
		return true;
	}
	
	@Override
	protected void doIt() throws Exception
	{
		if (!isAvailable())
			return;
		
		try
		{
			TableSettings workPlanTableSettings = TableSettings.findOrCreate(getProject(), getTabSpecificModelIdentifier());
			ObjectDataInputPanel codeListPanel = createEditorPanel(workPlanTableSettings);
			ModelessDialogWithClose dialog = new ModelessDialogWithClose(getMainWindow(), codeListPanel, getDialogTitle());
			dialog.setScrollableMainPanel(codeListPanel);
			getView().showFloatingPropertiesDialog(dialog);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	public static String getTabSpecificModelIdentifier()
	{
		return WorkPlanTreeTablePanel.getTabSpecificModelIdentifier();
	}

	abstract protected String getDialogTitle();
	abstract protected ObjectDataInputPanel createEditorPanel(TableSettings workPlanTableSettings);
}
