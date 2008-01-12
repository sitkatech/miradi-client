/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import org.conservationmeasures.eam.dialogs.activity.MethodListManagementPanel;
import org.conservationmeasures.eam.dialogs.base.ModalDialogWithClose;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.martus.swing.Utilities;

public class EditMethodsDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(getSelectedHierarchies().length != 1)
			return false;
		
		ORef indicatorRef = getSelectedHierarchies()[0].getRefForType(Indicator.getObjectType());
		if(indicatorRef == null || indicatorRef.isInvalid())
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			ORef indicatorRef = getPicker().getSelectionHierarchy().getRefForType(Indicator.getObjectType());
			MethodListManagementPanel panel = new MethodListManagementPanel(getProject(), getMainWindow(), indicatorRef, getMainWindow().getActions());
			ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), panel, EAM.text("Edit Methods"));
			Utilities.centerDlg(dialog);
			panel.updateSplitterLocation();
			dialog.setVisible(true);
			
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
	}


}
