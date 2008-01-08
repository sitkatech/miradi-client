/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.dialogs.base.ModalDialogWithClose;
import org.conservationmeasures.eam.dialogs.diagram.RelevancyIndicatorPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.martus.swing.Utilities;

public class EditIndicatorRelevancyListDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getSelectedHierarchies().length == 0)
			return false;
		
		
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		ORefList refList = getSelectedHierarchies()[0];
		ORef objectiveRef = refList.getRefForType(Objective.getObjectType());
		RelevancyIndicatorPanel indicatorPanel = new RelevancyIndicatorPanel(getProject(), objectiveRef);
		ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), indicatorPanel, EAM.text("Choose Indicator"));
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);
	}
}
