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

package org.miradi.views.planning.doers;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.planning.PlanningCustomizePanel;

public class PlanningCustomizeDialogPopupDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return true;
	}

	@Override
	public void doIt() throws Exception
	{
		if(!isAvailable())
			return;
		
		showCustomizeDialog(getMainWindow());
	}

	public static void showCustomizeDialog(MainWindow mainWindowToUse) throws Exception
	{
		ObjectDataInputPanel editor = new PlanningCustomizePanel(mainWindowToUse.getProject());
		ModalDialogWithClose dialog = new ModalDialogWithClose(mainWindowToUse, EAM.text("Customize..."));
		dialog.setScrollableMainPanel(editor);
		editor.becomeActive();
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);
	}
}
