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
		ObjectDataInputPanel panel = getPickListPanel(ref);
		ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), panel, getDialogTitle());
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);
	}
	
	abstract protected ORef getSelectionRef();

	abstract protected boolean isInvalidSelection();
	
	abstract protected String getDialogTitle();
	
	abstract protected ObjectDataInputPanel getPickListPanel(ORef objectiveRef);
}
