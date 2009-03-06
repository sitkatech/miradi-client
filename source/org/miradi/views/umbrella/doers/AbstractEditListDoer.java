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

import java.awt.Dimension;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.BaseObject;
import org.miradi.views.ObjectsDoer;
 
public abstract class AbstractEditListDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(!isProjectOpen())
			return false;
		return (getSelectedObject() != null);
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), getDialogTitle());
			dialog.setScrollableMainPanel(createEditPanel());
			setPreferredSizeOfDialog(dialog);
			Utilities.centerDlg(dialog);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	protected Dimension getDialogPreferredSize()
	{
		return null;
	}
	
	protected void setPreferredSizeOfDialog(ModalDialogWithClose dialog)
	{
		if (getDialogPreferredSize() != null)
			dialog.setPreferredSize(getDialogPreferredSize());
	}
		
	protected BaseObject getSelectedObject()
	{
		BaseObject singleSelected = getSingleSelected(getObjectType());
		if (singleSelected == null)
			return null;
			
	
		return singleSelected;
	}

	abstract protected int getObjectType();
	
	abstract protected DisposablePanel createEditPanel() throws Exception;
	
	abstract protected String getDialogTitle();
}
