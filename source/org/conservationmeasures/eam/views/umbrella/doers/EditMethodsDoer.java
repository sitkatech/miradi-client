/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import org.conservationmeasures.eam.dialogs.activity.MethodListManagementPanel;
import org.conservationmeasures.eam.dialogs.base.ObjectListManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Indicator;

public class EditMethodsDoer extends AbstractEditDoer
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

	protected String getDialogTitle()
	{
		return EAM.text("Edit Methods");
	}

	protected ObjectListManagementPanel getManagementPanel() throws Exception
	{
		ORef indicatorRef = getPicker().getSelectionHierarchy().getRefForType(Indicator.getObjectType());

		return new MethodListManagementPanel(getProject(), getMainWindow(), indicatorRef, getMainWindow().getActions());
	}
}
