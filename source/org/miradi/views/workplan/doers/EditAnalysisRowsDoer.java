/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.workplan.doers;

import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.main.EAM;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.workplan.EditAnalysisRowsPanel;

public class EditAnalysisRowsDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return super.isAvailable();
	}
	
	@Override
	public void doIt() throws Exception
	{
		if (!isAvailable())
			return;
		
		EditAnalysisRowsPanel panel = new EditAnalysisRowsPanel(getProject());
		ModelessDialogWithClose dialog = new ModelessDialogWithClose(getMainWindow(), panel, 
				EAM.text("Configure Work Plan Analysis"));
		dialog.setScrollableMainPanel(panel);
		getView().showFloatingPropertiesDialog(dialog);
	}
}
