/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class EditMethodsDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(getSelectedHierarchies().length != 1)
			return false;
		
		if(getSelectedHierarchies()[0].getRefForType(Indicator.getObjectType()) == null)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		EAM.notifyDialog(EAM.text("Not implemented yet"));
//		try
//		{
//			ORef indicatorRef = getPicker().getSelectionHierarchy().getRefForType(Indicator.getObjectType());
//			MethodListManagementPanel panel = new MethodListManagementPanel(getProject(), getMainWindow(), indicatorRef, getMainWindow().getActions());
//			ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), panel, EAM.text("Edit Methods"));
//			Utilities.centerDlg(dialog);
//			dialog.setVisible(true);
//			
//		}
//		catch(Exception e)
//		{
//			throw new CommandFailedException(e);
//		}
	}


}
