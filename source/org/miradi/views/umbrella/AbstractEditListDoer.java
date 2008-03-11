/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractEditListDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getSelectedHierarchies().length == 0)
			return false;
		
		if (isInvalidSelection())
			return false;
		
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		ORef ref = getSelectionRef();
		ObjectDataInputPanel panel = getRelevancyPanel(ref);
		ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), panel, getDialogTitle());
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);
	}
	
	abstract protected ORef getSelectionRef();

	abstract protected boolean isInvalidSelection();
	
	abstract protected String getDialogTitle();
	
	abstract protected ObjectDataInputPanel getRelevancyPanel(ORef objectiveRef);
}
