/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella.doers;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractPopUpEditDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(getSelectedHierarchies().length != 1)
			return false;
		
		ORef ref = getSelectedHierarchies()[0].getRefForType(getTypeToFilterOn());
		if(ref == null || ref.isInvalid())
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			ObjectListManagementPanel panel = getManagementPanel();
			ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), panel, getDialogTitle());
			Utilities.centerDlg(dialog);
			panel.updateSplitterLocation();
			dialog.setVisible(true);			
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	abstract protected int getTypeToFilterOn();

	abstract protected String getDialogTitle();

	abstract protected ObjectListManagementPanel getManagementPanel() throws Exception;
}
