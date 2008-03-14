/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella.doers;

import org.miradi.dialogs.activity.MethodListManagementPanel;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;

public class EditMethodsDoer extends AbstractPopUpEditDoer
{
	public EditMethodsDoer()
	{
		super(Indicator.getObjectType(), EAM.text("Edit Methods"));
	}

	protected ObjectListManagementPanel createManagementPanel() throws Exception
	{
		ORef indicatorRef = getSelectedHierarchies()[0].getRefForType(getTypeToFilterOn());

		return new MethodListManagementPanel(getMainWindow(), indicatorRef);
	}
}
