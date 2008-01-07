/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.dialogs.base.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.diagram.RelevancyIndicatorPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.martus.swing.Utilities;

public class EditIndicatorRelevancyListDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		RelevancyIndicatorPanel indicatorPanel = new RelevancyIndicatorPanel(getProject(), ORef.INVALID);
		ModelessDialogWithClose dialog = new ModelessDialogWithClose(getMainWindow(), indicatorPanel, EAM.text("Choose Indicator"));
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);
	}
}
