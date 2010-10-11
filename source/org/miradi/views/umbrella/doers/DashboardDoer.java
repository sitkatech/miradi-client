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

package org.miradi.views.umbrella.doers;

import org.martus.swing.Utilities;
import org.miradi.dialogs.dashboard.DashboardDialog;
import org.miradi.views.MainWindowDoer;

public class DashboardDoer extends MainWindowDoer
{
	@Override
	public boolean isAvailable()
	{
		return getProject().isOpen();
	}
	
	@Override
	public void doIt() throws Exception
	{
		if (!isAvailable())
			return;
		
		DashboardDialog dialog = new DashboardDialog(getMainWindow());
		Utilities.centerDlg(dialog);
		dialog.becomeActive();
		dialog.setVisible(true);
		dialog.becomeInactive();
	}
}
