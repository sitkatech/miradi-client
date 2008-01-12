/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import org.conservationmeasures.eam.dialogs.base.ModalDialogWithClose;
import org.conservationmeasures.eam.dialogs.base.ObjectListManagementPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.martus.swing.Utilities;

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
