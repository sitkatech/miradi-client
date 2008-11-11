/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.BaseObject;
import org.miradi.utils.ModalDialogWithCloseButNoMainScrollPane;
import org.miradi.views.ObjectsDoer;

//FIXME what is the difference between this class and AbstractEditListDoer?  notice the missing t in list 
public abstract class AbstractEditLisDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return (getSelectedObject() != null);
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			ModalDialogWithClose dialog = new ModalDialogWithCloseButNoMainScrollPane(getMainWindow(), getEditPanel(), getDialogTitle());
			setDialogPreferredSize(dialog);
			Utilities.centerDlg(dialog);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	protected void setDialogPreferredSize(ModalDialogWithClose dialog)
	{
	}
	
	protected BaseObject getSelectedObject()
	{
		BaseObject singleSelected = getSingleSelected(getObjectType());
		if (singleSelected == null)
			return null;
			
	
		return singleSelected;
	}

	abstract protected int getObjectType();
	
	abstract protected DisposablePanel getEditPanel() throws Exception;
	
	abstract protected String getDialogTitle();
}
