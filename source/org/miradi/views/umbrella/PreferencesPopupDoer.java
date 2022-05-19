/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.umbrella;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.PreferencesDialog;
import org.miradi.dialogs.base.PreferencesPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.views.ViewDoer;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.EventObject;

public class PreferencesPopupDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		return true;
	}

	@Override
	public void doIt(EventObject event) throws Exception
	{
		if (event instanceof ActionEvent)
		{
			ActionEvent actionEvent = (ActionEvent) event;
			menuAltKeyModifier = ((actionEvent.getModifiers() & InputEvent.ALT_MASK) != 0);
		}
		super.doIt(event);
	}

	@Override
	protected void doIt() throws Exception
	{		
		try
		{
			showPreferencesDialog();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	private void showPreferencesDialog() throws Exception
	{
		PreferencesPanel preferencesPanel = new PreferencesPanel(getMainWindow(), menuAltKeyModifier);
		PreferencesDialog dlg = new PreferencesDialog(getMainWindow(), preferencesPanel, EAM.text("Miradi Preferences"));
		dlg.pack();
		Utilities.centerDlg(dlg);
		dlg.setVisible(true);
	}

	private boolean menuAltKeyModifier = false;
}
